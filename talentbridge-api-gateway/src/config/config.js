require("dotenv").config();

const config = {
  port: process.env.PORT || 8080,

  springBootUrl: process.env.SPRING_BOOT_URL || "http://localhost:8081",
};

module.exports = config;
