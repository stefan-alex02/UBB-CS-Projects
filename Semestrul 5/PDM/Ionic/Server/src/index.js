const Koa = require('koa');
const app = new Koa();
const server = require('http').createServer(app.callback());
const WebSocket = require('ws');
const wss = new WebSocket.Server({ server });
const Router = require('koa-router');
const cors = require('koa-cors');
const bodyparser = require('koa-bodyparser');

app.use(bodyparser());
app.use(cors());
app.use(async (ctx, next) => {
  const start = new Date();
  await next();
  const ms = new Date() - start;
  console.log(`${ctx.method} ${ctx.url} ${ctx.response.status} - ${ms}ms`);
});

app.use(async (ctx, next) => {
  await new Promise(resolve => setTimeout(resolve, 2000));
  await next();
});

app.use(async (ctx, next) => {
  try {
    await next();
  } catch (err) {
    ctx.response.body = { message: err.message || 'Unexpected error' };
    ctx.response.status = 500;
  }
});

class Movie {
  constructor({ id, title, director, year, rating }) {
    this.id = id;
    this.title = title;
    this.director = director;
    this.year = year;
    this.rating = rating;
  }
}

const movies = [];
for (let i = 0; i < 3; i++) {
  movies.push(new Movie({ id: `${i}`, title: `Movie ${i}`, director: `Director ${i}`,
    ranting: 1 + Math.random() * 9, year: 1900 + Math.random() * 126, date: new Date(Date.now() + 1)}));
}
let lastMovie = movies[movies.length - 1];
const pageSize = 10;

const broadcast = data =>
  wss.clients.forEach(client => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify(data));
    }
  });

const router = new Router();

router.get('/movie', ctx => {
  ctx.response.body = items;
  ctx.response.status = 200;
});

router.get('/movie/:id', async (ctx) => {
  const movieId = ctx.request.params.id;
  const movie = movies.find(item => movieId === movie.id);
  if (movie) {
    ctx.response.body = movie;
    ctx.response.status = 200; // ok
  } else {
    ctx.response.body = { message: `Movie with id ${movieId} not found` };
    ctx.response.status = 404; // NOT FOUND (if you know the resource was deleted, then return 410 GONE)
  }
});

const createMovie = async (ctx) => {
  const movie = ctx.request.body;
  
  // validation:
  if (!movie.title) {
    ctx.response.body = { message: 'Title is missing' };
    ctx.response.status = 400; //  BAD REQUEST
    return;
  }
  if (!movie.director) {
    ctx.response.body = { message: 'Director is missing' };
    ctx.response.status = 400; //  BAD REQUEST
    return;
  }

  movie.id = `${lastMovie.id + 1}`;
  lastMovie = movie;
  movie.date = new Date();
  movies.push(item);
  ctx.response.body = item;
  ctx.response.status = 201; // CREATED
  broadcast({ event: 'created', payload: { movie } });
};

router.post('/movie', async (ctx) => {
  await createMovie(ctx);
});

router.put('/movie/:id', async (ctx) => {
  const id = ctx.params.id;
  const movie = ctx.request.body;
  movie.date = new Date();
  const movieId = movie.id;
  if (movieId && id !== movie.id) {
    ctx.response.body = { message: `Param id and body id should be the same` };
    ctx.response.status = 400; // BAD REQUEST
    return;
  }
  if (!movieId) {
    await createMovie(ctx);
    return;
  }
  const index = movies.findIndex(item => item.id === id);
  if (index === -1) {
    ctx.response.body = { message: `Movie with id ${id} not found` };
    ctx.response.status = 400; // BAD REQUEST
    return;
  }
  // const itemVersion = parseInt(ctx.request.get('ETag')) || item.version;
  // if (itemVersion < items[index].version) {
  //   ctx.response.body = { message: `Version conflict` };
  //   ctx.response.status = 409; // CONFLICT
  //   return;
  // }
  // item.version++;
  movies[index] = movie;
  lastMovie = movie;
  ctx.response.body = item;
  ctx.response.status = 200; // OK
  broadcast({ event: 'updated', payload: { movie } });
});

router.del('/item/:id', ctx => {
  const id = ctx.params.id;
  const index = items.findIndex(item => id === item.id);
  if (index !== -1) {
    const item = items[index];
    items.splice(index, 1);
    lastUpdated = new Date();
    broadcast({ event: 'deleted', payload: { item } });
  }
  ctx.response.status = 204; // no content
});

setInterval(() => {
  lastUpdated = new Date();
  lastId = lastMovie.id + 1;
  const movie = new Movie({...lastMovie, date: lastUpdated, id: lastId});
  lastMovie = movie;
  movies.push(movie);
  console.log(`New movie: ${movie.title}`);
  broadcast({ event: 'created', payload: { movie } });
}, 5000);

app.use(router.routes());
app.use(router.allowedMethods());

server.listen(3000);
