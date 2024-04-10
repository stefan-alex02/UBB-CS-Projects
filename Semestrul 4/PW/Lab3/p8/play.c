#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
 
struct data {
  int nr;
  int tries;
};
 
int getIdFromCookie() {
  int id;
  sscanf(getenv("HTTP_COOKIE"), "sessionToken=%d", &id);
  return id;  
}
 
int getNumberFromQueryString() {
  char buffer[2048];
  int nr;
  strcpy(buffer, getenv("QUERY_STRING"));
  sscanf(buffer, "nr=%d", &nr);
  return nr;  
}
 
int init() { 
  int r, id;
  int code;
  char filename[100];
  struct data d;
  
  srand(getpid());
  r = random() % 100;
 
  do {
    id = random();
    sprintf(filename, "/tmp/%d.txt", id);
    code = creat(filename, O_CREAT | O_EXCL | 0600);
  }
  while (code < 0);
 
  d.nr = r;
  d.tries = 0;
  write(code, &d, sizeof(d));
  close(code);
  
  return id;
}
 
void destroy(int id) {
  char filename[100];
  sprintf(filename, "/tmp/%d.txt", id);
  unlink(filename);
}
 
int getNumberFromFile(int id) {
  char filename[100];
  int fd;
  
  sprintf(filename, "/tmp/%d.txt", id);
  struct data d;
  fd = open(filename, O_RDWR);
  if (fd < 0)
    return -1;
  read(fd, &d, sizeof(d));
  d.tries++;
  lseek(fd, 0, SEEK_SET);
  write(fd, &d, sizeof(d));  
  close(fd);
  return d.nr;
}
 
int getNoOfTries(int id) {
  char filename[100];
  int fd;
  sprintf(filename, "/tmp/%d.txt", id);
  struct data d;
  fd = open(filename, O_RDONLY);
  read(fd, &d, sizeof(d));
  close(fd);
  return d.tries;    
}
 
int isNewUser() {
  char* cookie = getenv("HTTP_COOKIE");

  if (cookie == NULL || strcmp(cookie, "") == 0) {
 	return 1;
  }
  
  /*if (getenv("QUERY_STRING") == NULL)
    return 1;
  if (strcmp(getenv("QUERY_STRING"), "") == 0)
    return 1;*/
  return 0;  
}
 
void printForm(int id) {
  printf("<form action='play.cgi' method='get'>\n");
  //printf("<input type='hidden' name='id' value='%d'>\n", id);
  printf("Nr: <input type='text' name='nr'><br>\n");
  printf("<input type='submit' value='Trimite'>\n");
  printf("</form>");
}
 
int main() {
  int id, status;
  if (isNewUser()) {
    id = init();    
    status = 0;
  }
  else {
    int nr, nr2;
    id = getIdFromCookie();
    nr = getNumberFromQueryString();
    nr2 = getNumberFromFile(id);
    if (nr2 == -1)
      status = 1;
    else if (nr == nr2)
      status = 2;
    else if (nr < nr2)
      status = 3;
    else if (nr > nr2)
       status = 4;                
  }

  printf("Content-type: text/html\n");

  if (status != 1 && status != 2) {
    printf("Set-Cookie: sessionToken=%d\n\n", id);
  }
  else {
    printf("Set-Cookie: sessionToken=%d; Expires=Thu, 01 Jan 1970 00:00:00 GMT\n\n", id);
  }

  printf("<html>\n<body>\n");

  printf("COOKIE: %d<br>\n", id);

  switch (status) {
    case 0 : printf("Ati inceput un joc nou.<br>\n"); printForm(id); break;
    case 1 : printf("Eroare. Click <a href='play.cgi'>here</a> for a new game!"); break;
    case 2 : printf("Ati ghicit din %d incercari. Click <a href='play.cgi'>here</a> for a new game!</body></html>", getNoOfTries(id)); destroy(id); break;
    case 3 : printf("Prea mic!<br>\n"); printForm(id); break;
    case 4 : printf("Prea mare!<br>\n"); printForm(id);
  }
  
  printf("</body>\n</html>");
}
