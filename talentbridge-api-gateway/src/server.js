const express = require("express");
const cors = require("cors");

const config = require("./config/config");
const proxyRoutes = require("./routes/proxyRoutes");

const app = express();

app.use(cors());

app.get("/health", (req, res) => {
  res.json({
    status: "Gateway running",
  });
});

app.use("/", proxyRoutes);

app.use(
  express.json({
    limit: "10mb",
  }),
);

app.listen(config.port, () => {
  console.log(`TalentBridge Gateway running on port ${config.port}`);
});
