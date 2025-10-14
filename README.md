# Smart Task Planner

Smart Task Planner is a web application that helps users break down their goals into actionable tasks with timelines using AI reasoning (Google Gemini API). Each task includes a name, description, start and end dates, and status.  

![Demo GIF](./assets/demo.gif)  

---

## Table of Contents
- [About](#about)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [API Endpoint](#api-endpoint)

---

## About
Smart Task Planner allows users to input any goal and automatically generates a structured list of tasks using Google gemini-2.0-flash model. Tasks are organized with estimated start and end dates, dependencies, and status (Pending/Completed).  

It’s perfect for project planning, personal productivity, or small team task management.

---

## Features
- Input a goal and generate actionable tasks.
- Each task includes:
  - **Task Name**
  - **Task Description**
  - **Start & End Dates**
  - **Status** (Pending/Completed)
- Polished card-based UI with React, Material UI, and Framer Motion.
- Backend built with Spring Boot.
- AI-powered task generation using Google gemini-2.0-flash model.

---

## Tech Stack
- **Frontend:** React, Material UI
- **Backend:** Java, Spring Boot
- **Database:** H2
- **AI Integration:** Google Gemini API
- **HTTP Client:** OkHttp
- **Version Control:** Git & GitHub

---

## API Endpoint

- **API:**
  ```bash
  GET /planner?goal="goal text"
- **Response Example:**
  ```bash
  [
  {
    "id": 0,
    "taskName": "Finalize Product Features",
    "taskDescription": "Confirm the final set of features to be included in the initial product launch.",
    "goal": "Launch a product in 2 weeks",
    "startDate": "2024-01-29",
    "endDate": "2024-01-30",
    "status": "PENDING"
  },
  {
    "id": 1,
    "taskName": "Complete Product Development",
    "taskDescription": "Finish coding, testing, and debugging all product features.",
    "goal": "Launch a product in 2 weeks",
    "startDate": "2024-01-30",
    "endDate": "2024-02-02",
    "status": "PENDING"
  }
]
