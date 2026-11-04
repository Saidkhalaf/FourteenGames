FROM node:24 as build

WORKDIR /app

COPY package*.json ./
RUN npm install
# Copy entire app into container
COPY . .

RUN npm run build

FROM nginx

COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]