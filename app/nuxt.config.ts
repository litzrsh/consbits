// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: "2024-04-03",
  devtools: { enabled: false },
  app: {
    head: {
      title: "Consbits",
      link: [
        {
          rel: "icon",
          type: "image/x-icon",
          href: "/favicon.png",
          media: "(prefers-color-scheme: light)",
        },
        {
          rel: "icon",
          type: "image/x-icon",
          href: "/favicon_dark.png",
          media: "(prefers-color-scheme: dark)",
        },
        { rel: "stylesheet", href: "/css/styles.css" },
        { rel: "stylesheet", href: "/fontawesome/css/all.min.css" },
      ],
    },
  },
});
