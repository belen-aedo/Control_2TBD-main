FROM node:20-alpine AS build
WORKDIR /app

# Permite definir la API al momento de build (se inyecta en Vite en tiempo de build)
ARG VITE_API_URL=http://localhost:8081/api
ENV VITE_API_URL=${VITE_API_URL}

COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
