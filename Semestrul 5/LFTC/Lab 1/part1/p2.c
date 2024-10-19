#include <stdio.h>

void prog1() {
    float pi = 3.14159;

    int r;
    scanf("%d", &r);

    float a = pi * r * r;
    
    float p = pi * r * 2;
    printf("%f %f", a, p);
}

void prog2() {
    int a, b;
    scanf("%d", &a);
    scanf("%d", &b);

    while (a != b) {
        if (a > b) {
            a = a - b;
        }
        else {
            b = b - a;
        }
    }

    printf("%d", a);
}

void prog3() {
    int n;
    scanf("%d", &n);

    int s = 0;

    int x;
    while (n > 0) {
        scanf("%d", &x);
        s = s + x;

        n = n - 1;
    }

    printf("%d", s);
}

int main() {
    // prog1();
    // prog2();
    prog3();

    return 0;
}