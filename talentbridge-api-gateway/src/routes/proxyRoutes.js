const express = require("express");
const { createProxyMiddleware } = require("http-proxy-middleware");

const config = require("../config/config");

const router = express.Router();

router.use(
  "/",
  createProxyMiddleware({
    target: config.springBootUrl,

    changeOrigin: true,

    xfwd: true,

    onProxyReq(proxyReq, req) {
      console.log("Gateway forwarding:", req.method, req.originalUrl);

      if (req.headers.authorization) {
        proxyReq.setHeader("Authorization", req.headers.authorization);
      }
    },

    onProxyRes(proxyRes, req) {
      console.log("Backend response:", proxyRes.statusCode);
    },

    onError(err, req, res) {
      console.log("Gateway error:", err.message);

      res.status(503).json({
        message: "Backend unavailable",
      });
    },

    timeout: 30000,

    proxyTimeout: 30000,
  }),
);

module.exports = router;
