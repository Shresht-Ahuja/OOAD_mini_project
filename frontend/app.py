"""
Music Management System — Streamlit Frontend
Calls the Spring Boot REST API at http://localhost:8080/api
"""

import streamlit as st
import requests
import json

API = "http://localhost:8080/api"

# ── Page config ───────────────────────────────────────────────────────────────
st.set_page_config(
    page_title="🎵 Music Management System",
    page_icon="🎵",
    layout="wide",
    initial_sidebar_state="expanded",
)

# ── Custom CSS ────────────────────────────────────────────────────────────────
st.markdown("""
<style>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');

html, body, [class*="css"] {
    font-family: 'Inter', sans-serif;
}

/* Dark gradient background */
.stApp {
    background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);
    color: #f0f0f0;
}

/* Sidebar */
section[data-testid="stSidebar"] {
    background: rgba(255,255,255,0.05);
    backdrop-filter: blur(12px);
    border-right: 1px solid rgba(255,255,255,0.1);
}

/* Cards */
.music-card {
    background: rgba(255,255,255,0.07);
    border: 1px solid rgba(255,255,255,0.12);
    border-radius: 16px;
    padding: 18px 22px;
    margin-bottom: 14px;
    backdrop-filter: blur(8px);
    transition: transform 0.2s, box-shadow 0.2s;
}
.music-card:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 30px rgba(139,92,246,0.3);
}

/* Song playing indicator */
.playing-badge {
    display:inline-block;
    background: linear-gradient(90deg,#8b5cf6,#ec4899);
    color:white;
    padding:2px 10px;
    border-radius:20px;
    font-size:12px;
    font-weight:600;
    animation: pulse 1.5s infinite;
}
@keyframes pulse {
    0%,100% { opacity:1; }
    50%      { opacity:0.5; }
}

/* Like badge */
.liked-badge {
    color:#ec4899;
    font-size:18px;
}

/* Section headers */
.section-title {
    font-size:24px;
    font-weight:700;
    background: linear-gradient(90deg, #8b5cf6, #ec4899);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    margin-bottom: 16px;
}

/* Star rating */
.stars { color:#f59e0b; font-size:18px; }

/* Success/error toast style */
.toast-success {
    background: rgba(16,185,129,0.2);
    border: 1px solid #10b981;
    border-radius: 8px;
    padding: 8px 14px;
    color: #6ee7b7;
}
.toast-error {
    background: rgba(239,68,68,0.2);
    border: 1px solid #ef4444;
    border-radius: 8px;
    padding: 8px 14px;
    color: #fca5a5;
}

/* Metrics */
div[data-testid="metric-container"] {
    background: rgba(255,255,255,0.06);
    border: 1px solid rgba(255,255,255,0.1);
    border-radius:12px;
    padding:12px;
}

/* Buttons */
.stButton > button {
    background: linear-gradient(135deg, #8b5cf6, #6d28d9);
    color: white;
    border: none;
    border-radius: 8px;
    font-weight: 600;
    transition: all 0.2s;
}
.stButton > button:hover {
    background: linear-gradient(135deg, #7c3aed, #5b21b6);
    transform: translateY(-1px);
    box-shadow: 0 4px 15px rgba(139,92,246,0.4);
}
</style>
""", unsafe_allow_html=True)

# ── Session state ─────────────────────────────────────────────────────────────
if "user" not in st.session_state:
    st.session_state.user = None
if "now_playing" not in st.session_state:
    st.session_state.now_playing = None

# ── Helper functions ──────────────────────────────────────────────────────────
def api_get(path, params=None):
    try:
        r = requests.get(f"{API}{path}", params=params, timeout=5)
        return r.json() if r.ok else []
    except Exception:
        return []

def api_post(path, data=None):
    try:
        r = requests.post(f"{API}{path}", json=data, timeout=5)
        return r.json(), r.ok
    except Exception as e:
        return {"error": str(e)}, False

def api_put(path, data=None):
    try:
        r = requests.put(f"{API}{path}", json=data, timeout=5)
        return r.json(), r.ok
    except Exception as e:
        return {"error": str(e)}, False

def api_delete(path):
    try:
        r = requests.delete(f"{API}{path}", timeout=5)
        return r.json(), r.ok
    except Exception as e:
        return {"error": str(e)}, False

def star_str(n):
    return "⭐" * n + "☆" * (5 - n)

def fmt_dur(sec):
    sec = int(sec)
    return f"{sec//60}:{sec%60:02d}"

def is_admin():
    return st.session_state.user and st.session_state.user.get("role") == "ADMIN"

def current_user_id():
    return st.session_state.user["userId"] if st.session_state.user else None

# ── Sidebar ───────────────────────────────────────────────────────────────────
with st.sidebar:
    st.markdown("## 🎵 MusicApp")
    st.divider()

    if st.session_state.user:
        u = st.session_state.user
        st.markdown(f"**👤 {u['name']}**")
        st.caption(f"{u['email']} · {u['role']}")
        st.divider()

        page = st.radio("Navigate", [
            "🏠 Home",
            "🎵 Songs",
            "📀 Albums",
            "🎤 Artists",
            "📋 Playlists",
            "⭐ Reviews",
            "🔧 Admin Panel" if is_admin() else None,
        ], label_visibility="collapsed")
        page = [p for p in [page] if p][0]

        st.divider()
        if st.button("🚪 Logout"):
            st.session_state.user = None
            st.session_state.now_playing = None
            st.rerun()
    else:
        page = "login"

    # Now Playing bar
    if st.session_state.now_playing:
        st.divider()
        np = st.session_state.now_playing
        st.markdown(f'<div class="playing-badge">▶ NOW PLAYING</div>', unsafe_allow_html=True)
        st.markdown(f"**{np['title']}**")
        st.caption(f"{np['genre']} · {fmt_dur(np['duration'])}")
        if st.button("⏸ Pause"):
            api_post(f"/songs/{np['songId']}/pause")
            st.session_state.now_playing = None
            st.rerun()

# ══════════════════════════════════════════════════════════════════════════════
# LOGIN / REGISTER PAGE
# ══════════════════════════════════════════════════════════════════════════════
if not st.session_state.user:
    st.markdown('<h1 style="text-align:center;background:linear-gradient(90deg,#8b5cf6,#ec4899);-webkit-background-clip:text;-webkit-text-fill-color:transparent;">🎵 Music Management System</h1>', unsafe_allow_html=True)
    st.markdown('<p style="text-align:center;color:#a0aec0;">Your premium music experience</p>', unsafe_allow_html=True)
    st.divider()

    col1, col2, col3 = st.columns([1, 2, 1])
    with col2:
        tab_login, tab_reg = st.tabs(["🔑 Login", "✨ Register"])

        with tab_login:
            with st.form("login_form"):
                email = st.text_input("Email", placeholder="you@example.com")
                pwd   = st.text_input("Password", type="password")
                submitted = st.form_submit_button("Login →", use_container_width=True)
                if submitted:
                    data, ok = api_post("/users/login", {"email": email, "password": pwd})
                    if ok:
                        st.session_state.user = data
                        st.rerun()
                    else:
                        st.error(data.get("error", "Login failed"))
            st.caption("Demo: admin@music.com / admin123")

        with tab_reg:
            with st.form("reg_form"):
                name  = st.text_input("Full Name")
                email = st.text_input("Email", placeholder="you@example.com", key="reg_email")
                pwd   = st.text_input("Password", type="password", key="reg_pwd")
                role  = st.selectbox("Role", ["USER", "ADMIN"])
                submitted = st.form_submit_button("Create Account →", use_container_width=True)
                if submitted:
                    data, ok = api_post("/users/register", {
                        "name": name, "email": email, "password": pwd, "role": role
                    })
                    if ok:
                        st.success("Account created! Please login.")
                    else:
                        st.error(data.get("error", "Registration failed"))

    st.stop()

# ══════════════════════════════════════════════════════════════════════════════
# HOME PAGE
# ══════════════════════════════════════════════════════════════════════════════
if page == "🏠 Home":
    st.markdown('<div class="section-title">🏠 Dashboard</div>', unsafe_allow_html=True)
    songs   = api_get("/songs")
    artists = api_get("/artists")
    albums  = api_get("/albums")
    reviews = api_get("/reviews")

    c1, c2, c3, c4 = st.columns(4)
    c1.metric("🎵 Songs",   len(songs))
    c2.metric("🎤 Artists", len(artists))
    c3.metric("📀 Albums",  len(albums))
    c4.metric("⭐ Reviews", len(reviews))

    st.divider()
    st.markdown("### 🔥 Recently Added Songs")
    for s in songs[-3:]:
        with st.container():
            st.markdown(f"""
            <div class="music-card">
                <b>{s['title']}</b> &nbsp;
                {'<span class="playing-badge">▶ PLAYING</span>' if s.get('playing') else ''}
                {'<span class="liked-badge">❤</span>' if s.get('liked') else ''}<br>
                <span style="color:#a0aec0">🎸 {s['genre']} &nbsp;|&nbsp; ⏱ {fmt_dur(s['duration'])}</span>
            </div>
            """, unsafe_allow_html=True)

    st.markdown("### 🎤 Artists on Platform")
    cols = st.columns(min(len(artists), 3))
    for i, a in enumerate(artists[:3]):
        with cols[i]:
            st.markdown(f"""
            <div class="music-card" style="text-align:center;">
                <div style="font-size:40px">🎤</div>
                <b>{a['name']}</b><br>
                <span style="color:#a0aec0;font-size:13px">{a['bio'][:60]}...</span>
            </div>
            """, unsafe_allow_html=True)

# ══════════════════════════════════════════════════════════════════════════════
# SONGS PAGE
# ══════════════════════════════════════════════════════════════════════════════
elif page == "🎵 Songs":
    st.markdown('<div class="section-title">🎵 Songs Library</div>', unsafe_allow_html=True)

    col_search, col_genre = st.columns([3, 1])
    with col_search:
        search = st.text_input("🔍 Search songs...", placeholder="Title keyword")
    with col_genre:
        genre_filter = st.text_input("🎸 Genre filter")

    params = {}
    if search:
        params["search"] = search
    elif genre_filter:
        params["genre"] = genre_filter

    songs = api_get("/songs", params)
    st.caption(f"{len(songs)} song(s) found")
    st.divider()

    for s in songs:
        with st.container():
            c1, c2, c3, c4 = st.columns([4, 1, 1, 1])
            with c1:
                badge = '<span class="playing-badge">▶ NOW PLAYING</span>' if (
                    st.session_state.now_playing and
                    st.session_state.now_playing.get("songId") == s["songId"]
                ) else ""
                heart = "❤" if s.get("liked") else "🤍"
                st.markdown(f"""
                <div class="music-card">
                    <b style="font-size:16px">{s['title']}</b> {badge}<br>
                    <span style="color:#a0aec0">🎸 {s['genre']} &nbsp;|&nbsp; ⏱ {fmt_dur(s['duration'])} &nbsp;|&nbsp; {heart} {s.get('likeCount',0)}</span>
                </div>
                """, unsafe_allow_html=True)
            with c2:
                if st.button("▶ Play", key=f"play_{s['songId']}"):
                    res, ok = api_post(f"/songs/{s['songId']}/play")
                    if ok:
                        st.session_state.now_playing = s
                        st.success(res.get("status", "Playing!"))
                        st.rerun()
            with c3:
                if st.button("❤ Like", key=f"like_{s['songId']}"):
                    res, ok = api_post(f"/songs/{s['songId']}/like")
                    if ok:
                        st.rerun()
            with c4:
                if is_admin() and st.button("🗑", key=f"del_song_{s['songId']}"):
                    api_delete(f"/songs/{s['songId']}")
                    st.rerun()

    if is_admin():
        st.divider()
        st.markdown("### ➕ Upload New Song")
        artists = api_get("/artists")
        artist_map = {a["name"]: a["artistId"] for a in artists}
        with st.form("upload_song"):
            c1, c2 = st.columns(2)
            title    = c1.text_input("Title")
            sel_art  = c2.selectbox("Artist", list(artist_map.keys()))
            duration = c1.number_input("Duration (sec)", min_value=30, value=180)
            genre    = c2.text_input("Genre")
            filepath = st.text_input("File Path", value="files/song.mp3")
            if st.form_submit_button("Upload Song"):
                _, ok = api_post("/songs", {
                    "artistId": artist_map[sel_art],
                    "title": title, "duration": duration,
                    "genre": genre, "filePath": filepath
                })
                if ok:
                    st.success("Song uploaded!")
                    st.rerun()

# ══════════════════════════════════════════════════════════════════════════════
# ALBUMS PAGE
# ══════════════════════════════════════════════════════════════════════════════
elif page == "📀 Albums":
    st.markdown('<div class="section-title">📀 Albums</div>', unsafe_allow_html=True)
    albums = api_get("/albums")

    for al in albums:
        with st.expander(f"📀 {al['title']}  —  Released: {al.get('releaseDate','?')}"):
            songs = al.get("songs", [])
            st.caption(f"{len(songs)} track(s)")
            for s in songs:
                st.markdown(f"&nbsp;&nbsp;🎵 **{s['title']}** · {fmt_dur(s['duration'])} · {s['genre']}")
            if is_admin() and st.button("🗑 Delete Album", key=f"del_al_{al['albumId']}"):
                api_delete(f"/albums/{al['albumId']}")
                st.rerun()

    if is_admin():
        st.divider()
        st.markdown("### ➕ Create Album")
        artists = api_get("/artists")
        artist_map = {a["name"]: a["artistId"] for a in artists}
        with st.form("create_album"):
            c1, c2 = st.columns(2)
            title     = c1.text_input("Album Title")
            sel_art   = c2.selectbox("Artist", list(artist_map.keys()), key="alb_art")
            rel_date  = c1.date_input("Release Date")
            if st.form_submit_button("Create Album"):
                _, ok = api_post("/albums", {
                    "artistId": artist_map[sel_art],
                    "title": title,
                    "releaseDate": str(rel_date)
                })
                if ok:
                    st.success("Album created!")
                    st.rerun()

# ══════════════════════════════════════════════════════════════════════════════
# ARTISTS PAGE
# ══════════════════════════════════════════════════════════════════════════════
elif page == "🎤 Artists":
    st.markdown('<div class="section-title">🎤 Artists</div>', unsafe_allow_html=True)
    artists = api_get("/artists")

    cols = st.columns(min(len(artists), 3) or 1)
    for i, a in enumerate(artists):
        with cols[i % 3]:
            songs  = a.get("songs", [])
            albums = a.get("albums", [])
            st.markdown(f"""
            <div class="music-card">
                <div style="font-size:36px;text-align:center">🎤</div>
                <h3 style="text-align:center;margin:8px 0">{a['name']}</h3>
                <p style="color:#a0aec0;font-size:13px">{a['bio']}</p>
                <hr style="border-color:rgba(255,255,255,0.1)">
                <span style="font-size:13px">🎵 {len(songs)} songs &nbsp;|&nbsp; 📀 {len(albums)} albums</span>
            </div>
            """, unsafe_allow_html=True)
            if is_admin():
                if st.button(f"🗑 Remove", key=f"del_art_{a['artistId']}"):
                    api_delete(f"/artists/{a['artistId']}")
                    st.rerun()

    if is_admin():
        st.divider()
        st.markdown("### ➕ Add Artist")
        with st.form("add_artist"):
            c1, c2 = st.columns(2)
            name = c1.text_input("Artist Name")
            bio  = c2.text_input("Bio")
            if st.form_submit_button("Add Artist"):
                _, ok = api_post("/artists", {"name": name, "bio": bio})
                if ok:
                    st.success("Artist added!")
                    st.rerun()

# ══════════════════════════════════════════════════════════════════════════════
# PLAYLISTS PAGE
# ══════════════════════════════════════════════════════════════════════════════
elif page == "📋 Playlists":
    st.markdown('<div class="section-title">📋 My Playlists</div>', unsafe_allow_html=True)
    uid  = current_user_id()
    pls  = api_get(f"/playlists/user/{uid}")
    all_songs = api_get("/songs")
    song_map  = {s["title"]: s["songId"] for s in all_songs}

    if not pls:
        st.info("You have no playlists yet. Create one below!")

    for pl in pls:
        with st.expander(f"📋 {pl['name']}  ({len(pl.get('songs',[]))} songs)  — Created: {pl.get('createdDate','?')}"):
            songs = pl.get("songs", [])
            for s in songs:
                c1, c2 = st.columns([5, 1])
                c1.markdown(f"🎵 **{s['title']}** · {fmt_dur(s['duration'])} · {s['genre']}")
                if c2.button("➖", key=f"rm_{pl['playlistId']}_{s['songId']}"):
                    api_delete(f"/playlists/{pl['playlistId']}/songs/{s['songId']}")
                    st.rerun()

            st.divider()
            c1, c2, c3 = st.columns(3)
            sel = c1.selectbox("Add song", list(song_map.keys()), key=f"add_s_{pl['playlistId']}")
            if c2.button("➕ Add", key=f"btn_add_{pl['playlistId']}"):
                api_post(f"/playlists/{pl['playlistId']}/songs/{song_map[sel]}")
                st.rerun()
            if c3.button("🔀 Shuffle", key=f"shuf_{pl['playlistId']}"):
                api_post(f"/playlists/{pl['playlistId']}/shuffle")
                st.rerun()
            if st.button("🗑 Delete Playlist", key=f"del_pl_{pl['playlistId']}"):
                api_delete(f"/playlists/{pl['playlistId']}")
                st.rerun()

    st.divider()
    st.markdown("### ➕ Create New Playlist")
    with st.form("create_pl"):
        pl_name = st.text_input("Playlist Name")
        if st.form_submit_button("Create"):
            _, ok = api_post("/playlists", {"userId": uid, "name": pl_name})
            if ok:
                st.success("Playlist created!")
                st.rerun()

# ══════════════════════════════════════════════════════════════════════════════
# REVIEWS PAGE
# ══════════════════════════════════════════════════════════════════════════════
elif page == "⭐ Reviews":
    st.markdown('<div class="section-title">⭐ Reviews</div>', unsafe_allow_html=True)
    songs = api_get("/songs")
    song_map = {s["title"]: s["songId"] for s in songs}
    sel_song_title = st.selectbox("Select Song", list(song_map.keys()))
    sel_song_id    = song_map.get(sel_song_title)

    if sel_song_id:
        reviews = api_get(f"/reviews/song/{sel_song_id}")
        st.caption(f"{len(reviews)} review(s) for **{sel_song_title}**")

        for rv in reviews:
            with st.container():
                c1, c2, c3 = st.columns([5, 1, 1])
                with c1:
                    st.markdown(f"""
                    <div class="music-card">
                        <span class="stars">{star_str(rv['rating'])}</span>
                        &nbsp; <b>User #{rv['userId']}</b>
                        <br>
                        <span style="color:#e2e8f0">{rv['comment']}</span>
                        <br>
                        <span style="color:#718096;font-size:12px">{rv.get('createdAt','')[:16]}</span>
                    </div>
                    """, unsafe_allow_html=True)
                uid = current_user_id()
                if rv["userId"] == uid:
                    with c2:
                        if st.button("✏️", key=f"edit_rv_{rv['reviewId']}"):
                            st.session_state[f"editing_{rv['reviewId']}"] = True
                    with c3:
                        if st.button("🗑", key=f"del_rv_{rv['reviewId']}"):
                            api_delete(f"/reviews/{rv['reviewId']}")
                            st.rerun()

                if st.session_state.get(f"editing_{rv['reviewId']}"):
                    with st.form(f"edit_form_{rv['reviewId']}"):
                        new_rating  = st.slider("Rating", 1, 5, rv["rating"])
                        new_comment = st.text_area("Comment", rv["comment"])
                        if st.form_submit_button("Save"):
                            api_put(f"/reviews/{rv['reviewId']}", {
                                "rating": new_rating, "comment": new_comment,
                                "userId": uid, "songId": sel_song_id
                            })
                            st.session_state[f"editing_{rv['reviewId']}"] = False
                            st.rerun()

        st.divider()
        st.markdown("### ✍️ Write a Review")
        with st.form("add_review"):
            rating  = st.slider("Your Rating", 1, 5, 5)
            comment = st.text_area("Your Comment", placeholder="What did you think?")
            if st.form_submit_button("Submit Review"):
                _, ok = api_post("/reviews", {
                    "userId": current_user_id(),
                    "songId": sel_song_id,
                    "rating": rating,
                    "comment": comment
                })
                if ok:
                    st.success("Review submitted!")
                    st.rerun()

# ══════════════════════════════════════════════════════════════════════════════
# ADMIN PANEL
# ══════════════════════════════════════════════════════════════════════════════
elif page == "🔧 Admin Panel" and is_admin():
    st.markdown('<div class="section-title">🔧 Admin Panel</div>', unsafe_allow_html=True)

    tab_users, tab_log = st.tabs(["👥 Manage Users", "📜 Activity Log"])

    with tab_users:
        users = api_get("/users")
        st.caption(f"{len(users)} registered user(s)")
        for u in users:
            c1, c2 = st.columns([5, 1])
            c1.markdown(f"""
            <div class="music-card">
                <b>{u['name']}</b> &nbsp;
                <span style="background:{'#6d28d9' if u['role']=='ADMIN' else '#1d4ed8'};
                       color:white;padding:2px 8px;border-radius:12px;font-size:12px">{u['role']}</span><br>
                <span style="color:#a0aec0">{u['email']}</span>
            </div>
            """, unsafe_allow_html=True)
            if u["userId"] != current_user_id():
                if c2.button("🗑", key=f"del_u_{u['userId']}"):
                    api_delete(f"/users/{u['userId']}")
                    st.rerun()

    with tab_log:
        st.markdown("### 📜 Song Event Activity Log (Observer Pattern)")
        log = api_get("/songs/activity-log")
        if not log:
            st.info("No events logged yet. Play or like some songs!")
        for entry in reversed(log[-30:]):
            st.code(entry, language=None)
