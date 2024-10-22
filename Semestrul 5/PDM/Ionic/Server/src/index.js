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
  constructor({ id, title, director, year, rating, date}) {
    this.id = id;
    this.title = title;
    this.director = director;
    this.rating = rating;
    this.year = year;
    this.date = date;
  }
}

const movies = [];
for (let i = 0; i < 3; i++) {
  movies.push(new Movie({ id: `${i}`,
    title: `Movie ${i}`,
    director: `Director ${i}`,
    rating: Math.floor(1 + Math.random() * 9),
    year: Math.floor(1900 + Math.random() * 126),
    date: new Date(Date.now() + 1)}));
}
let lastUpdated = new Date();
let largestId = 3;
const pageSize = 10;

const broadcast = data =>
  wss.clients.forEach(client => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify(data));
    }
  });

const router = new Router();

router.get('/movie', ctx => {
  ctx.response.body = movies;
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

  movie.id = `${largestId + 1}`;
  largestId = parseInt(movie.id);
  movie.date = new Date();
  movies.push(movie);
  ctx.response.body = movie;
  ctx.response.status = 201; // CREATED
  broadcast({ event: 'created', payload: { movie } });
};

router.post('/movie', async (ctx) => {
  console.log('POST /movie', ctx.request.body)
  await createMovie(ctx);
});

router.put('/movie/:id', async (ctx) => {
  const id = ctx.params.id;
  const movie = ctx.request.body;
  movie.date = new Date();
  const movieId = movie.id;
  console.log('PUT /movie', movie, id, movieId)
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
  ctx.response.body = movie;
  ctx.response.status = 200; // OK
  broadcast({ event: 'updated', payload: { movie } });
});

router.del('/movie/:id', ctx => {
  const id = ctx.params.id;
  const index = movies.findIndex(item => id === item.id);
  if (index !== -1) {
    const item = movies[index];
    movies.splice(index, 1);
    lastUpdated = new Date();
    broadcast({ event: 'deleted', payload: { item } });
  }
  ctx.response.status = 204; // no content
});

setInterval(() => {
  lastUpdated = new Date();
  const movie = new Movie({id: largestId,
    title: `Movie ${largestId}`,
    director: `Director ${largestId}`,
    rating: Math.floor(1 + Math.random() * 9),
    year: Math.floor(1900 + Math.random() * 126),
    date: lastUpdated});
  largestId++;
  movies.push(movie);
  console.log(`New movie: ${movie.title} by ${movie.director}, year ${movie.year} with rating ${movie.rating}`);
  broadcast({ event: 'created', payload: { movie } });
}, 10000);

app.use(router.routes());
app.use(router.allowedMethods());

server.listen(3000);
