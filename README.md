# RouteIQ - Smart Multi-Modal Route Optimization Platform

RouteIQ is a high-performance, intelligent route optimization and navigation web application. It features real-time multi-modal route calculations (Driving, Walking, Cycling), Google Maps-style place search autocomplete, device GPS location detection, and interactive dark-themed map visualization.

---

## Table of Contents
- [Features](#-key-features)
- [Tech Stack](#-technology-stack)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [How to Run the Project](#-how-to-run-the-project)
  - [1. Running the Backend](#1-running-the-backend-spring-boot)
  - [2. Running the Frontend](#2-running-the-frontend-react--vite)
- [API Endpoints Reference](#-api-endpoints-reference)
- [H2 Database Console](#-h2-in-memory-database-console)
- [User Guide](#-how-to-use-the-website)
- [Troubleshooting & FAQs](#-troubleshooting--faqs)

---

## Key Features

1. **Google Maps-Style Place Autocomplete**:
   - Real-time search-as-you-type suggestions with debouncing (280ms).
   - Powered by OpenStreetMap and Photon API for lightning-fast matching of cities, landmarks, and addresses.
   - Keyboard navigable (`Arrow Down`, `Arrow Up`, `Enter`, `Escape`) with one-click clear button (`×`).

2. **Device Current Location Detection (GPS)**:
   - One-click **"Your current location"** option at the top of the Origin search dropdown.
   - Uses browser Geolocation API with high-accuracy GPS coordinates.
   - Automatic reverse-geocoding via backend to resolve street and city names.
   - Interactive pulsing blue GPS dot marker on the map with smooth auto-pan.

3. **Realistic Multi-Modal Travel Modes**:
   - Supports **DRIVING**, **WALKING**, and **CYCLING**.
   - Accurately scales travel time based on realistic speed profiles (OSRM car base duration, walking ~10x, cycling ~3x).

4. **Flexible Optimization Goals**:
   - **Fastest (Time)**: Minimizes overall travel duration.
   - **Shortest (Distance)**: Minimizes total distance in kilometers.
   - **Balanced**: Optimizes for trade-off between time and distance.
   - **Eco-Friendly (Fuel)**: Prioritizes fuel and energy conservation.

5. **Interactive Dark-Themed Map**:
   - Built with React-Leaflet and OpenStreetMap tiles styled in a high-contrast dark theme.
   - Automatic bounding-box fit (`fitBounds`) to display complete route paths.
   - Color-coded route lines, origin & destination popups, and route summary overlay.

6. **Zero-Configuration In-Memory Database**:
   - Runs out-of-the-box with H2 in-memory database—no external database setup required.

---

## Technology Stack

| Layer | Technologies |
| :--- | :--- |
| **Frontend** | React 19, TypeScript, Vite, React-Leaflet, Leaflet, Lucide React, Axios |
| **Backend** | Java 21 / 25, Spring Boot 4, Spring Data JPA, Spring Security, Hibernate |
| **Routing & Geocoding** | OSRM (Open Source Routing Machine), OpenStreetMap Nominatim, Photon |
| **Database** | H2 In-Memory Database (with JPA/Hibernate entity auto-DDL) |
| **Styling** | Vanilla CSS Design Tokens, Glassmorphism, Dark Mode Palette |

---

## Project Structure

```text
ROUTEIQ/
├── README.md                      # Complete project documentation (this file)
└── routeiq/
    ├── backend/                   # Spring Boot Java Backend
    │   ├── mvnw / mvnw.cmd        # Maven wrapper scripts
    │   ├── pom.xml                # Backend dependencies and plugins
    │   └── src/
    │       ├── main/
    │       │   ├── java/com/routeiq/
    │       │   │   ├── RouteiqApplication.java
    │       │   │   ├── geocoding/     # Autocomplete & reverse geocoding
    │       │   │   ├── routing/       # OSRM route calculation & modes
    │       │   │   ├── security/      # WebSecurityConfig & JWT handlers
    │       │   │   └── user/          # User entities & repositories
    │       │   └── resources/
    │       │       └── application-dev.yml
    │       └── test/
    └── frontend/                  # React + Vite Frontend
        ├── index.html             # RouteIQ HTML entry with custom SVG favicon
        ├── package.json           # Frontend dependencies
        ├── vite.config.ts         # Vite bundler configuration
        ├── public/
        │   └── favicon.svg        # Cyan RouteIQ navigation pointer icon
        └── src/
            ├── components/
            │   ├── MapView.tsx            # Leaflet map & GPS blue dot marker
            │   ├── PlaceAutocomplete.tsx  # Google Maps-style search dropdown
            │   └── Sidebar.tsx            # Route settings, inputs & modes
            ├── pages/
            │   └── Dashboard.tsx          # Main application dashboard
            ├── App.tsx
            └── index.css                  # Dark mode styling & animations
```

---

## Prerequisites

Before running the application, make sure you have the following installed on your machine:
- **Java Development Kit (JDK)**: Version 17, 21, or higher (`java -version`)
- **Node.js & npm**: Node.js 18+ and npm 9+ (`node -v`, `npm -v`)
- **Internet Connection**: Required for fetching OpenStreetMap tiles and OSRM routing data.

---

## How to Run the Project

### 1. Running the Backend (Spring Boot)

Open a terminal or command prompt:

```bash
# Navigate to the backend directory
cd routeiq/backend

# Run the Spring Boot application using Maven wrapper
# On Windows (PowerShell / CMD):
cmd /c mvnw.cmd spring-boot:run

# On Linux / macOS:
./mvnw spring-boot:run
```

- **Backend Base URL:** `http://localhost:8080/`
- Once started, you will see `Started RouteiqApplication in X.XXX seconds`.

---

### 2. Running the Frontend (React + Vite)

Open a **separate** terminal window:

```bash
# Navigate to the frontend directory
cd routeiq/frontend

# Install dependencies (only needed the first time)
npm install

# Start the Vite development server
# On Windows (PowerShell / CMD):
cmd /c npm run dev

# On Linux / macOS:
npm run dev
```

- **Frontend Website URL:** **`http://localhost:5173/`**

Open your web browser and navigate to **[http://localhost:5173/](http://localhost:5173/)** to use RouteIQ!

---

## 🔌 API Endpoints Reference

All backend endpoints are hosted on port `8080`. Public routing and geocoding endpoints are accessible without authentication:

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/v1/routes/calculate` | Calculates routes between origin & destination coordinates with travel modes. |
| `GET` | `/api/v1/geocoding/autocomplete?q={query}` | Search-as-you-type autocomplete returning matching cities and coordinates. |
| `GET` | `/api/v1/geocoding/search?q={query}` | Exact forward geocoding query returning location coordinates. |
| `GET` | `/api/v1/geocoding/reverse?lat={lat}&lon={lon}` | Reverse geocodes coordinates to street and city display name. |
| `GET` | `/api/v1/health` | Health check endpoint. |

### Sample Route Calculation Payload (`POST /api/v1/routes/calculate`):
```json
{
  "origin": {
    "name": "Coimbatore, Tamil Nadu, India",
    "latitude": 11.0018,
    "longitude": 76.9628
  },
  "destination": {
    "name": "Chennai, Tamil Nadu, India",
    "latitude": 13.0801,
    "longitude": 80.2808
  },
  "travelMode": "DRIVING",
  "optimizationMode": "BALANCED",
  "trafficMode": "SIMULATED",
  "avoidTolls": false,
  "avoidClosures": false
}
```

---

## H2 In-Memory Database Console

The application includes an embedded H2 database console for inspecting tables (`users`, `saved_routes`, `route_history`, `incidents`, etc.):

- **URL:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL:** `jdbc:h2:mem:routeiqdb`
- **User Name:** `SA`
- **Password:** *(leave blank)*

---

## How to Use the Website

1. **Origin Selection**:
   - **Device GPS**: Click the **"Your current location"** option at the top of the Origin dropdown to automatically detect and show your device location on the map with a pulsing blue GPS dot.
   - **Manual Search**: Type any city or place (e.g., `coimb`, `mumb`, `delhi`) and select from the dropdown suggestions.
2. **Destination Selection**:
   - Type any destination place or landmark into the Destination input and click the suggestion.
3. **Select Travel Mode**:
   - Choose between **DRIVING**, **WALKING**, or **CYCLING**. Notice how travel durations adjust realistically for each mode.
4. **Choose Optimization Goal**:
   - Select **Fastest**, **Shortest**, **Balanced**, or **Eco-friendly**.
5. **Calculate & View**:
   - Click **Calculate Route**. The route geometry will render across the dark-themed map, and a summary card in the bottom right will show total distance in km and estimated time in minutes.

---

## Troubleshooting & FAQs

#### 1. Windows PowerShell: `running scripts is disabled on this system`
- **Cause**: Windows PowerShell security policy restricts `.ps1` and batch script execution.
- **Solution**: Run commands prefixed with `cmd /c`:
  ```bash
  cmd /c mvnw.cmd spring-boot:run
  cmd /c npm run dev
  ```
  Or run PowerShell as Administrator and execute:
  ```powershell
  Set-ExecutionPolicy -Scope CurrentUser RemoteSigned
  ```

#### 2. Port `8080` or `5173` is already in use
- Stop any existing background terminal running the servers or terminate the process holding the port using:
  ```bash
  netstat -ano | findstr :8080
  taskkill /PID <PID> /F
  ```

#### 3. Browser Location Permission Denied
- Ensure you click **Allow** when your browser prompts: *"http://localhost:5173 wants to know your location"*.
- If blocked, click the site settings padlock icon in the browser address bar and enable Location permissions.
