FROM node:13-alpine

WORKDIR /app

RUN npm i -g typescript

CMD npm install @types/node && tsc --types node --strict --outDir /tmp solution.ts && node /tmp/solution.js
