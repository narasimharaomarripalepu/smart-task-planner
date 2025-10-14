import React, { useState } from "react";
import axios from "axios";
import {
  Box,
  Card,
  CardContent,
  Typography,
  CircularProgress,
  Chip,
  TextField,
  Button,
} from "@mui/material";
import { motion } from "framer-motion";

const PlannerPage = () => {
  const [goal, setGoal] = useState("");
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchTasks = () => {
    if (!goal) return;
    setLoading(true);
    axios
      .get(`http://localhost:8080/planner?goal=${goal}`)
      .then((res) => {
        setTasks(res.data);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setLoading(false);
      });
  };

  return (
    <Box sx={{ p: 3, minHeight: "100vh", backgroundColor: "#f5f5f5" }}>
      {/* Input Section */}
      <Box
        sx={{
          display: "flex",
          flexDirection: { xs: "column", sm: "row" },
          mb: 4,
          gap: 2,
          alignItems: "center",
        }}
      >
        <TextField
          fullWidth
          label="Enter your goal"
          value={goal}
          onChange={(e) => setGoal(e.target.value)}
        />
        <Button variant="contained" onClick={fetchTasks} sx={{ height: 56 }}>
          Generate Tasks
        </Button>
      </Box>

      {/* Loading */}
      {loading && (
        <Box display="flex" justifyContent="center" mt={4}>
          <CircularProgress />
        </Box>
      )}

      {/* Tasks Grid */}
      <Box
        sx={{
          display: "grid",
          gridTemplateColumns: { xs: "1fr", sm: "1fr 1fr", md: "1fr 1fr 1fr" },
          gap: 3,
        }}
      >
        {tasks.map((task, index) => (
          <motion.div
            key={index}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: index * 0.05 }}
          >
            <Card
              sx={{
                minHeight: 250,
                display: "flex",
                flexDirection: "column",
                justifyContent: "space-between",
                borderRadius: 3,
                boxShadow: 3,
                "&:hover": {
                  transform: "translateY(-5px)",
                  boxShadow: 6,
                  transition: "0.3s",
                },
              }}
            >
              <CardContent>
                <Typography variant="h6" gutterBottom fontWeight="bold">
                  {task.taskName}
                </Typography>
                <Typography variant="body2" color="text.secondary" mb={1}>
                  {task.taskDescription}
                </Typography>
                <Typography variant="caption" display="block" mb={1}>
                  Goal: {task.goal}
                </Typography>
                <Typography variant="caption" display="block">
                  Start: {task.startDate} | End: {task.endDate}
                </Typography>
              </CardContent>
              <Box p={2}>
                <Chip
                  label={task.status}
                  color={task.status === "COMPLETED" ? "success" : "warning"}
                  sx={{ fontWeight: "bold" }}
                />
              </Box>
            </Card>
          </motion.div>
        ))}
      </Box>
    </Box>
  );
};

export default PlannerPage;
