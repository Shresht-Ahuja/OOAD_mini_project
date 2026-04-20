# 🎵 Music Management System

A complete **OOP-based Music Management System** built with:
- **Backend**: Java 21 + Spring Boot 3 (REST API, MVC architecture)
- **Frontend**: Python Streamlit
- **Storage**: In-memory (HashMap-based repositories)

---

## 🚀 How to Run

### Step 1 — Start the Spring Boot Backend (IntelliJ IDEA)

1. Open **IntelliJ IDEA**
2. Click **File → Open** and select the `ooad_mini_project` folder
3. IntelliJ will detect `pom.xml` and auto-import Maven (click **Trust Project** if asked)
4. Wait for Maven to download dependencies (~1 min on first run)
5. Open `src/main/java/com/musicapp/MusicAppApplication.java`
6. Click the green **▶ Run** button (or press `Shift+F10`)
7. The server starts at **http://localhost:8080**

> You will see `✅ Music Management System started — seed data loaded.` in the console.

---

### Step 2 — Start the Streamlit Frontend

Open a terminal in the `frontend/` folder:

```bash
cd frontend
pip install -r requirements.txt
streamlit run app.py
```

The UI opens at **http://localhost:8501**

---

### Step 3 — Login

| Role  | Email               | Password  |
|-------|---------------------|-----------|
| Admin | admin@music.com     | admin123  |
| User  | Register a new one  | any       |

---

## 🏗️ Project Structure

```
ooad_mini_project/
├── pom.xml                          ← Spring Boot 3 Maven config
├── mvnw.cmd                         ← Maven wrapper (no Maven install needed)
├── frontend/
│   ├── app.py                       ← Streamlit UI
│   └── requirements.txt
└── src/main/java/com/musicapp/
    ├── MusicAppApplication.java     ← Entry point + seed data
    ├── model/                       ← Domain classes (User, Admin, Artist,
    │                                   Album, Song, Playlist, Review)
    ├── repository/                  ← In-memory data stores
    ├── service/                     ← Business logic layer
    ├── controller/                  ← REST API controllers
    ├── dto/                         ← Request/Response DTOs
    ├── factory/                     ← UserFactory (Creational pattern)
    ├── observer/                    ← SongEventListener (Behavioral pattern)
    └── decorator/                   ← SongDecorator (Structural pattern)
```

---

## 🎨 Design Patterns Implemented

| Pattern | Category | Implementation |
|---------|----------|----------------|
| **Factory Method** | Creational | `UserFactory.createUser()` — creates User or Admin based on role string |
| **Decorator** | Structural | `LikedSongDecorator` — wraps Song to add liked badge without modifying Song |
| **Observer** | Behavioral | `ActivityLogListener` — notified on every play/pause/like event |
| **Repository** | Structural (Spring) | All repositories encapsulate in-memory data access |

---

## 📐 Design Principles Applied

| Principle | Where |
|-----------|-------|
| **Single Responsibility** | Each class has one reason to change |
| **Open/Closed** | Song extended via Decorator; UserFactory extended for new roles |
| **Liskov Substitution** | `Admin` used anywhere `User` is expected |
| **Dependency Inversion** | Controllers/Services depend on abstractions, not concrete stores |

---

## 📡 REST API Summary

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users/register` | Register user |
| POST | `/api/users/login` | Login |
| GET  | `/api/songs` | List / search songs |
| POST | `/api/songs/{id}/play` | Play song |
| POST | `/api/songs/{id}/like` | Like/unlike song |
| GET  | `/api/artists` | List artists |
| GET  | `/api/albums` | List albums |
| GET  | `/api/playlists/user/{userId}` | User playlists |
| POST | `/api/playlists/{id}/shuffle` | Shuffle playlist |
| POST | `/api/reviews` | Add review |
| PUT  | `/api/reviews/{id}` | Edit review |

---

## 🗂️ Class Relationships

```
User (1) ──────────────→ (many) Playlist
User (1) ──────────────→ (many) Review
Admin extends User
Artist (1) ────────────→ (many) Album
Artist (1) ────────────→ (many) Song
Album (1) ─────────────→ (many) Song
Playlist (many) ───────↔ (many) Song
Song (1) ──────────────→ (many) Review
```
