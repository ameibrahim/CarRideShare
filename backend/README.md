This is the backend for the Car Ride Share App.

First, run the development server:

```bash
npm run dev
# or
yarn dev
# or
pnpm dev
# or
bun dev
```

Open [http://localhost:3000](http://localhost:3000)
The API documentation is available below.


This backend will most likely not run with a UI.
The backend is deployed with docker

Run `docker compose up -d` to compose all systems up with a mysql database and prisma ORM.

Run `docker compose down` to compose all systems down