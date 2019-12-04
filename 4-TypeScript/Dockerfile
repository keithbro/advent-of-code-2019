FROM node:13-alpine

WORKDIR /app

RUN npm install -g typescript

CMD tsc --outDir /tmp solution.ts && node /tmp/solution.js
