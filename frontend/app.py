"""
Music Management System — Streamlit Frontend
Calls the Spring Boot REST API at http://localhost:8080/api
Spotify-inspired UI
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
    initial_sidebar_state="collapsed",
)

# ── Custom CSS (Spotify-inspired) ─────────────────────────────────────────────
st.markdown("""
<style>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap');

* {
    font-family: 'Inter', sans-serif;
}

/* Main background - Spotify dark */
.stApp {
    background: linear-gradient(180deg, #1a1a1a 0%, #0f0f0f 100%);
}

/* Hide default Streamlit elements */
#MainMenu {visibility: hidden;}
header {visibility: hidden;}
footer {visibility: hidden;}

/* Custom Navbar */
.spotify-navbar {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    background: rgba(0, 0, 0, 0.95);
    backdrop-filter: blur(10px);
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    padding: 0 32px;
    z-index: 1000;
    height: 64px;
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.nav-logo {
    display: flex;
    align-items: center;
    gap: 12px;
    font-size: 24px;
    font-weight: 800;
    background: linear-gradient(135deg, #1DB954, #1ed760);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
}

.nav-links {
    display: flex;
    gap: 8px;
    align-items: center;
    flex: 1;
    margin-left: 48px;
}

.nav-link {
    padding: 8px 20px;
    border-radius: 24px;
    font-weight: 500;
    font-size: 14px;
    color: #b3b3b3;
    transition: all 0.2s ease;
    cursor: pointer;
    background: transparent;
    border: none;
}

.nav-link:hover {
    color: #ffffff;
    background: rgba(255, 255, 255, 0.1);
}

.nav-link.active {
    color: #ffffff;
    background: #1DB954;
}

.nav-user {
    display: flex;
    align-items: center;
    gap: 16px;
    background: rgba(0, 0, 0, 0.7);
    padding: 6px 12px 6px 8px;
    border-radius: 40px;
    cursor: pointer;
    transition: all 0.2s;
}

.nav-user:hover {
    background: rgba(255, 255, 255, 0.1);
}

.user-avatar {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    background: linear-gradient(135deg, #1DB954, #1ed760);
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 700;
    font-size: 14px;
}

.user-info {
    font-size: 14px;
    color: white;
}

.user-role {
    font-size: 11px;
    color: #b3b3b3;
}

/* Main content padding for navbar */
.main-content {
    padding-top: 80px;
    padding-left: 32px;
    padding-right: 32px;
}

/* Cards */
.song-card, .album-card, .artist-card {
    background: #181818;
    border-radius: 8px;
    padding: 16px;
    transition: all 0.3s ease;
    cursor: pointer;
    border: 1px solid transparent;
}

.song-card:hover, .album-card:hover, .artist-card:hover {
    background: #282828;
    transform: translateY(-4px);
}

.song-image {
    width: 100%;
    aspect-ratio: 1;
    background: linear-gradient(135deg, #1DB954, #1ed760);
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 48px;
    margin-bottom: 16px;
}

.song-title {
    font-weight: 600;
    color: white;
    margin-bottom: 6px;
    font-size: 16px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.song-artist {
    font-size: 14px;
    color: #b3b3b3;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

/* Play button overlay */
.card-wrapper {
    position: relative;
}

.play-overlay {
    position: absolute;
    bottom: 12px;
    right: 12px;
    background: #1DB954;
    width: 48px;
    height: 48px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    transition: all 0.2s ease;
    box-shadow: 0 8px 16px rgba(0,0,0,0.3);
    cursor: pointer;
}

.card-wrapper:hover .play-overlay {
    opacity: 1;
    transform: scale(1.05);
}

/* Now playing bar */
.now-playing-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: #181818;
    border-top: 1px solid rgba(255,255,255,0.1);
    padding: 12px 32px;
    z-index: 1000;
}

/* Section headers */
.section-header {
    display: flex;
    justify-content: space-between;
    align-items: baseline;
    margin-bottom: 24px;
}

.section-title {
    font-size: 32px;
    font-weight: 800;
    color: white;
    letter-spacing: -0.5px;
}

.section-link {
    color: #b3b3b3;
    font-size: 13px;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 1px;
    cursor: pointer;
}

.section-link:hover {
    color: white;
    text-decoration: underline;
}

/* Buttons */
.stButton > button {
    background: #1DB954;
    color: white;
    border: none;
    border-radius: 500px;
    font-weight: 700;
    font-size: 14px;
    padding: 8px 32px;
    transition: all 0.2s;
}

.stButton > button:hover {
    background: #1ed760;
    transform: scale(1.02);
}

/* Metrics */
div[data-testid="metric-container"] {
    background: #181818;
    border-radius: 8px;
    padding: 20px;
    border: 1px solid #282828;
}

/* Expander */
.streamlit-expanderHeader {
    background: #181818;
    border-radius: 8px;
    color: white;
}

/* Text inputs */
.stTextInput > div > div > input, .stTextArea > div > div > textarea {
    background: #3e3e3e;
    color: white;
    border: none;
    border-radius: 4px;
}

.stTextInput > div > div > input:focus {
    border-color: #1DB954;
}

/* Selectbox */
.stSelectbox > div > div {
    background: #3e3e3e;
    color: white;
}

/* Tabs */
.stTabs [data-baseweb="tab-list"] {
    gap: 8px;
}

.stTabs [data-baseweb="tab"] {
    background: #181818;
    border-radius: 500px;
    padding: 8px 24px;
    color: #b3b3b3;
}

.stTabs [aria-selected="true"] {
    background: #1DB954;
    color: white;
}

/* Scrollbar */
::-webkit-scrollbar {
    width: 12px;
    background: #181818;
}

::-webkit-scrollbar-thumb {
    background: #535353;
    border-radius: 6px;
}

::-webkit-scrollbar-thumb:hover {
    background: #7f7f7f;
}
</style>
""", unsafe_allow_html=True)

# ── Session state ─────────────────────────────────────────────────────────────
if "user" not in st.session_state:
    st.session_state.user = None
if "now_playing" not in st.session_state:
    st.session_state.now_playing = None
if "page" not in st.session_state:
    st.session_state.page = "Home"

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

def fmt_dur(sec):
    sec = int(sec)
    return f"{sec//60}:{sec%60:02d}"

def is_admin():
    return st.session_state.user and st.session_state.user.get("role") == "ADMIN"

def is_artist():
    return st.session_state.user and st.session_state.user.get("role") == "ARTIST"

def can_manage_content():
    return is_admin() or is_artist()

def current_user_id():
    return st.session_state.user["userId"] if st.session_state.user else None

# ── Custom Navbar Component ───────────────────────────────────────────────────
def render_navbar():
    if not st.session_state.user:
        return
    
    u = st.session_state.user
    
    # Navbar HTML
    if is_artist():
        nav_items = ["Home", "Songs", "Albums"]
    else:
        nav_items = ["Home", "Songs", "Albums", "Artists", "Playlists", "Reviews"]
        
    if is_admin():
        nav_items.append("Admin")
    
    nav_html = f"""
    <div class="spotify-navbar">
        <div class="nav-logo">
            🎵 MusicFlow
        </div>
        <div class="nav-links">
    """
    
    for item in nav_items:
        active_class = "active" if st.session_state.page == item else ""
        nav_html += f'<button class="nav-link {active_class}" onclick="window.parent.postMessage({{type: "streamlit:setComponentValue", value: "{item}"}}, "*")">{item}</button>'
    
    nav_html += f"""
        </div>
        <div class="nav-user">
            <div class="user-avatar">{u['name'][0].upper()}</div>
            <div class="user-info">
                <div>{u['name']}</div>
                <div class="user-role">{u['role']}</div>
            </div>
        </div>
    </div>
    """
    
    st.markdown(nav_html, unsafe_allow_html=True)
    
    # Handle navigation via columns (workaround for button clicks)
    cols = st.columns([1] * len(nav_items))
    for idx, item in enumerate(nav_items):
        if cols[idx].button(item, key=f"nav_{item}", use_container_width=True):
            st.session_state.page = item
            st.rerun()
    
    # Logout button in a separate column
    if st.button("Logout", key="nav_logout"):
        st.session_state.user = None
        st.session_state.now_playing = None
        st.rerun()

# ── Now Playing Bar ───────────────────────────────────────────────────────────
def render_now_playing():
    if st.session_state.now_playing:
        np = st.session_state.now_playing
        st.markdown(f"""
        <div class="now-playing-bar">
            <div style="display: flex; align-items: center; gap: 16px;">
                <div style="background: #1DB954; width: 56px; height: 56px; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 28px;">🎵</div>
                <div style="flex: 1;">
                    <div style="color: white; font-weight: 600;">{np['title']}</div>
                    <div style="color: #b3b3b3; font-size: 12px;">{np['genre']} • {fmt_dur(np['duration'])}</div>
                </div>
                <div>
                    <div style="display: flex; gap: 16px;">
                        <span style="cursor: pointer; color: white;">⏸</span>
                        <span style="cursor: pointer; color: white;">⏭</span>
                    </div>
                </div>
            </div>
        </div>
        """, unsafe_allow_html=True)
        
        col1, col2, col3 = st.columns([3, 1, 1])
        if col2.button("⏸ Pause", key="pause_btn"):
            api_post(f"/songs/{np['songId']}/pause")
            st.session_state.now_playing = None
            st.rerun()

# ══════════════════════════════════════════════════════════════════════════════
# LOGIN / REGISTER PAGE (Styled)
# ══════════════════════════════════════════════════════════════════════════════
if not st.session_state.user:
    st.markdown("""
    <div style="min-height: 100vh; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, #1a1a1a 0%, #0f0f0f 100%);">
        <div style="background: rgba(0,0,0,0.8); padding: 48px; border-radius: 16px; max-width: 500px; width: 100%;">
            <div style="text-align: center; margin-bottom: 32px;">
                <div style="font-size: 64px;">🎵</div>
                <h1 style="background: linear-gradient(135deg, #1DB954, #1ed760); -webkit-background-clip: text; -webkit-text-fill-color: transparent; margin-top: 16px;">MusicFlow</h1>
                <p style="color: #b3b3b3;">Your premium music experience</p>
            </div>
    """, unsafe_allow_html=True)
    
    tab_login, tab_reg = st.tabs(["Sign In", "Sign Up"])
    
    with tab_login:
        with st.form("login_form"):
            email = st.text_input("Email", placeholder="you@example.com")
            pwd = st.text_input("Password", type="password")
            submitted = st.form_submit_button("Sign In", use_container_width=True)
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
            name = st.text_input("Full Name")
            email = st.text_input("Email", placeholder="you@example.com", key="reg_email")
            pwd = st.text_input("Password", type="password", key="reg_pwd")
            role = st.selectbox("Role", ["USER", "ARTIST", "ADMIN"])
            submitted = st.form_submit_button("Create Account", use_container_width=True)
            if submitted:
                data, ok = api_post("/users/register", {
                    "name": name, "email": email, "password": pwd, "role": role
                })
                if ok:
                    st.success("Account created! Please sign in.")
                else:
                    st.error(data.get("error", "Registration failed"))
    
    st.markdown("</div></div>", unsafe_allow_html=True)
    st.stop()

# ── Render Navbar and Main Content ───────────────────────────────────────────
render_navbar()
st.markdown('<div class="main-content">', unsafe_allow_html=True)

# ══════════════════════════════════════════════════════════════════════════════
# HOME PAGE
# ══════════════════════════════════════════════════════════════════════════════
if st.session_state.page == "Home":
    songs = api_get("/songs")
    artists = api_get("/artists")
    albums = api_get("/albums")
    
    # Stats row
    col1, col2, col3, col4 = st.columns(4)
    col1.metric("Total Songs", len(songs))
    col2.metric("Artists", len(artists))
    col3.metric("Albums", len(albums))
    col4.metric("Playlists", len(api_get(f"/playlists/user/{current_user_id()}")))
    
    st.markdown("<br>", unsafe_allow_html=True)
    
    # Featured Songs (Grid layout)
    st.markdown('<div class="section-header"><div class="section-title">🔥 Featured Songs</div><div class="section-link">SEE ALL →</div></div>', unsafe_allow_html=True)
    
    cols = st.columns(5)
    for idx, song in enumerate(songs[:5]):
        with cols[idx]:
            st.markdown(f"""
            <div class="card-wrapper">
                <div class="song-card">
                    <div class="song-image">🎵</div>
                    <div class="song-title">{song['title'][:25]}</div>
                    <div class="song-artist">{song['genre']}</div>
                </div>
                <div class="play-overlay" onclick="window.parent.postMessage({{type: 'streamlit:setComponentValue', value: 'play_{song['songId']}'}}, '*')">▶</div>
            </div>
            """, unsafe_allow_html=True)
            
            col_btn1, col_btn2 = st.columns(2)
            if col_btn1.button("▶ Play", key=f"home_play_{song['songId']}"):
                api_post(f"/songs/{song['songId']}/play")
                st.session_state.now_playing = song
                st.rerun()
            if col_btn2.button("❤", key=f"home_like_{song['songId']}"):
                api_post(f"/songs/{song['songId']}/like")
                st.rerun()
    
    st.markdown("<br><br>", unsafe_allow_html=True)
    
    # Popular Artists
    st.markdown('<div class="section-header"><div class="section-title">🎤 Popular Artists</div><div class="section-link">SEE ALL →</div></div>', unsafe_allow_html=True)
    
    cols = st.columns(5)
    for idx, artist in enumerate(artists[:5]):
        with cols[idx]:
            st.markdown(f"""
            <div class="artist-card" style="text-align: center;">
                <div style="font-size: 80px; margin-bottom: 12px;">🎤</div>
                <div class="song-title">{artist['name']}</div>
                <div class="song-artist">{artist.get('bio', 'Artist')[:40]}...</div>
            </div>
            """, unsafe_allow_html=True)

# ══════════════════════════════════════════════════════════════════════════════
# SONGS PAGE
# ══════════════════════════════════════════════════════════════════════════════
elif st.session_state.page == "Songs":
    st.markdown('<div class="section-title">All Songs</div>', unsafe_allow_html=True)
    st.markdown("<br>", unsafe_allow_html=True)
    
    col_search, col_genre = st.columns([3, 1])
    with col_search:
        search = st.text_input("🔍 Search songs...", placeholder="What do you want to listen to?")
    with col_genre:
        genre_filter = st.text_input("🎸 Filter by genre")
    
    params = {}
    if search:
        params["search"] = search
    elif genre_filter:
        params["genre"] = genre_filter
    
    songs = api_get("/songs", params)
    st.caption(f"Found {len(songs)} songs")
    
    # Grid display
    cols = st.columns(4)
    for idx, song in enumerate(songs):
        with cols[idx % 4]:
            st.markdown(f"""
            <div class="card-wrapper">
                <div class="song-card">
                    <div class="song-image">🎵</div>
                    <div class="song-title">{song['title'][:25]}</div>
                    <div class="song-artist">{song['genre']} • {fmt_dur(song['duration'])}</div>
                    <div class="song-artist">❤ {song.get('likeCount', 0)} likes</div>
                </div>
            </div>
            """, unsafe_allow_html=True)
            
            col1, col2, col3 = st.columns(3)
            if col1.button("▶", key=f"play_{song['songId']}"):
                api_post(f"/songs/{song['songId']}/play")
                st.session_state.now_playing = song
                st.rerun()
            if col2.button("❤", key=f"like_{song['songId']}"):
                api_post(f"/songs/{song['songId']}/like")
                st.rerun()
            if is_admin() and col3.button("🗑", key=f"del_{song['songId']}"):
                api_delete(f"/songs/{song['songId']}")
                st.rerun()
    
    if can_manage_content():
        st.markdown("---")
        st.markdown("### ➕ Add New Song")
        artists = api_get("/artists")
        artist_map = {a["name"]: a["artistId"] for a in artists}
        with st.form("add_song"):
            col1, col2 = st.columns(2)
            title = col1.text_input("Song Title")
            
            artist_names = list(artist_map.keys())
            default_artist_idx = 0
            if is_artist() and st.session_state.user['name'] in artist_map:
                default_artist_idx = artist_names.index(st.session_state.user['name'])
            
            artist = col2.selectbox("Artist", artist_names, index=default_artist_idx)
            duration = col1.number_input("Duration (seconds)", min_value=30, value=180)
            genre = col2.text_input("Genre")
            if st.form_submit_button("Add Song"):
                _, ok = api_post("/songs", {
                    "artistId": artist_map[artist],
                    "title": title,
                    "duration": duration,
                    "genre": genre,
                    "filePath": "files/song.mp3"
                })
                if ok:
                    st.success("Song added!")
                    st.rerun()

# ══════════════════════════════════════════════════════════════════════════════
# ALBUMS PAGE
# ══════════════════════════════════════════════════════════════════════════════
elif st.session_state.page == "Albums":
    st.markdown('<div class="section-title">Albums</div>', unsafe_allow_html=True)
    st.markdown("<br>", unsafe_allow_html=True)
    
    albums = api_get("/albums")
    
    cols = st.columns(3)
    for idx, album in enumerate(albums):
        with cols[idx % 3]:
            with st.expander(f"📀 {album['title']}"):
                st.markdown(f"**Released:** {album.get('releaseDate', 'Unknown')}")
                songs = album.get("songs", [])
                for song in songs:
                    st.markdown(f"• {song['title']} - {fmt_dur(song['duration'])}")
                
                if is_admin():
                    if st.button("Delete Album", key=f"del_album_{album['albumId']}"):
                        api_delete(f"/albums/{album['albumId']}")
                        st.rerun()
    
    if can_manage_content():
        st.markdown("---")
        st.markdown("### ➕ Create Album")
        artists = api_get("/artists")
        artist_map = {a["name"]: a["artistId"] for a in artists}
        with st.form("add_album"):
            title = st.text_input("Album Title")
            
            artist_names = list(artist_map.keys())
            default_artist_idx = 0
            if is_artist() and st.session_state.user['name'] in artist_map:
                default_artist_idx = artist_names.index(st.session_state.user['name'])
                
            artist = st.selectbox("Artist", artist_names, index=default_artist_idx)
            release_date = st.date_input("Release Date")
            if st.form_submit_button("Create Album"):
                _, ok = api_post("/albums", {
                    "artistId": artist_map[artist],
                    "title": title,
                    "releaseDate": str(release_date)
                })
                if ok:
                    st.success("Album created!")
                    st.rerun()

# ══════════════════════════════════════════════════════════════════════════════
# ARTISTS PAGE
# ══════════════════════════════════════════════════════════════════════════════
elif st.session_state.page == "Artists":
    st.markdown('<div class="section-title">Artists</div>', unsafe_allow_html=True)
    st.markdown("<br>", unsafe_allow_html=True)
    
    artists = api_get("/artists")
    
    cols = st.columns(3)
    for idx, artist in enumerate(artists):
        with cols[idx % 3]:
            st.markdown(f"""
            <div class="artist-card" style="text-align: center; padding: 24px;">
                <div style="font-size: 80px; margin-bottom: 16px;">🎤</div>
                <div class="song-title" style="font-size: 20px;">{artist['name']}</div>
                <div class="song-artist">{artist.get('bio', 'No bio available')}</div>
                <div class="song-artist" style="margin-top: 12px;">📀 {len(artist.get('albums', []))} albums • 🎵 {len(artist.get('songs', []))} songs</div>
            </div>
            """, unsafe_allow_html=True)
            
            if is_admin():
                if st.button("Remove Artist", key=f"remove_artist_{artist['artistId']}"):
                    api_delete(f"/artists/{artist['artistId']}")
                    st.rerun()
    
    if is_admin():
        st.markdown("---")
        st.markdown("### ➕ Add Artist")
        with st.form("add_artist"):
            name = st.text_input("Artist Name")
            bio = st.text_area("Bio")
            if st.form_submit_button("Add Artist"):
                _, ok = api_post("/artists", {"name": name, "bio": bio})
                if ok:
                    st.success("Artist added!")
                    st.rerun()

# ══════════════════════════════════════════════════════════════════════════════
# PLAYLISTS PAGE
# ══════════════════════════════════════════════════════════════════════════════
elif st.session_state.page == "Playlists":
    st.markdown('<div class="section-title">Your Library</div>', unsafe_allow_html=True)
    st.markdown("<br>", unsafe_allow_html=True)
    
    uid = current_user_id()
    playlists = api_get(f"/playlists/user/{uid}")
    all_songs = api_get("/songs")
    song_map = {s["title"]: s["songId"] for s in all_songs}
    
    if not playlists:
        st.info("✨ Create your first playlist to get started!")
    
    cols = st.columns(2)
    for idx, playlist in enumerate(playlists):
        with cols[idx % 2]:
            with st.expander(f"📋 {playlist['name']}  •  {len(playlist.get('songs', []))} songs"):
                songs = playlist.get("songs", [])
                for song in songs:
                    col1, col2 = st.columns([4, 1])
                    col1.markdown(f"**{song['title']}** - {fmt_dur(song['duration'])}")
                    if col2.button("❌", key=f"remove_{playlist['playlistId']}_{song['songId']}"):
                        api_delete(f"/playlists/{playlist['playlistId']}/songs/{song['songId']}")
                        st.rerun()
                
                st.markdown("---")
                col1, col2, col3 = st.columns(3)
                with col1:
                    new_song = st.selectbox("Add song", list(song_map.keys()), key=f"add_{playlist['playlistId']}")
                    if st.button("➕ Add", key=f"add_btn_{playlist['playlistId']}"):
                        api_post(f"/playlists/{playlist['playlistId']}/songs/{song_map[new_song]}")
                        st.rerun()
                with col2:
                    if st.button("🔀 Shuffle", key=f"shuffle_{playlist['playlistId']}"):
                        api_post(f"/playlists/{playlist['playlistId']}/shuffle")
                        st.success("Playlist shuffled!")
                        st.rerun()
                with col3:
                    if st.button("🗑 Delete Playlist", key=f"delete_{playlist['playlistId']}"):
                        api_delete(f"/playlists/{playlist['playlistId']}")
                        st.rerun()
    
    st.markdown("---")
    st.markdown("### Create New Playlist")
    with st.form("create_playlist"):
        name = st.text_input("Playlist Name", placeholder="My Awesome Playlist")
        if st.form_submit_button("Create Playlist"):
            _, ok = api_post("/playlists", {"userId": uid, "name": name})
            if ok:
                st.success("Playlist created!")
                st.rerun()

# ══════════════════════════════════════════════════════════════════════════════
# REVIEWS PAGE
# ══════════════════════════════════════════════════════════════════════════════
elif st.session_state.page == "Reviews":
    st.markdown('<div class="section-title">Reviews</div>', unsafe_allow_html=True)
    st.markdown("<br>", unsafe_allow_html=True)
    
    songs = api_get("/songs")
    song_map = {s["title"]: s["songId"] for s in songs}
    selected_song = st.selectbox("Select a song to review", list(song_map.keys()))
    
    if selected_song:
        song_id = song_map[selected_song]
        reviews = api_get(f"/reviews/song/{song_id}")
        
        st.markdown(f"### {len(reviews)} Reviews for '{selected_song}'")
        
        for review in reviews:
            with st.container():
                st.markdown(f"""
                <div style="background: #181818; border-radius: 8px; padding: 16px; margin-bottom: 12px;">
                    <div style="display: flex; align-items: center; gap: 12px; margin-bottom: 8px;">
                        <div style="background: #1DB954; width: 32px; height: 32px; border-radius: 50%; display: flex; align-items: center; justify-content: center;">⭐</div>
                        <div>
                            <strong style="color: white;">User #{review['userId']}</strong>
                            <div style="color: #f59e0b;">{'★' * review['rating']}{'☆' * (5 - review['rating'])}</div>
                        </div>
                    </div>
                    <div style="color: #b3b3b3;">{review['comment']}</div>
                    <div style="color: #535353; font-size: 12px; margin-top: 8px;">{review.get('createdAt', '')[:16]}</div>
                </div>
                """, unsafe_allow_html=True)
                
                if review["userId"] == current_user_id():
                    col1, col2 = st.columns(2)
                    if col1.button("Edit", key=f"edit_{review['reviewId']}"):
                        st.session_state[f"editing_{review['reviewId']}"] = True
                    if col2.button("Delete", key=f"delete_{review['reviewId']}"):
                        api_delete(f"/reviews/{review['reviewId']}")
                        st.rerun()
                
                if st.session_state.get(f"editing_{review['reviewId']}"):
                    with st.form(f"edit_form_{review['reviewId']}"):
                        new_rating = st.slider("Rating", 1, 5, review["rating"])
                        new_comment = st.text_area("Comment", review["comment"])
                        if st.form_submit_button("Save Changes"):
                            api_put(f"/reviews/{review['reviewId']}", {
                                "rating": new_rating,
                                "comment": new_comment,
                                "userId": current_user_id(),
                                "songId": song_id
                            })
                            st.session_state[f"editing_{review['reviewId']}"] = False
                            st.rerun()
        
        st.markdown("---")
        st.markdown("### Write a Review")
        with st.form("write_review"):
            rating = st.slider("Your Rating", 1, 5, 5)
            comment = st.text_area("Your Comment", placeholder="Share your thoughts...")
            if st.form_submit_button("Submit Review"):
                _, ok = api_post("/reviews", {
                    "userId": current_user_id(),
                    "songId": song_id,
                    "rating": rating,
                    "comment": comment
                })
                if ok:
                    st.success("Review submitted!")
                    st.rerun()

# ══════════════════════════════════════════════════════════════════════════════
# ADMIN PANEL
# ══════════════════════════════════════════════════════════════════════════════
elif st.session_state.page == "Admin" and is_admin():
    st.markdown('<div class="section-title">Admin Panel</div>', unsafe_allow_html=True)
    st.markdown("<br>", unsafe_allow_html=True)
    
    tab1, tab2 = st.tabs(["User Management", "Activity Log"])
    
    with tab1:
        users = api_get("/users")
        for user in users:
            col1, col2 = st.columns([4, 1])
            with col1:
                st.markdown(f"""
                <div style="background: #181818; border-radius: 8px; padding: 12px; margin-bottom: 8px;">
                    <strong style="color: white;">{user['name']}</strong>
                    <span style="background: {'#1DB954' if user['role'] == 'ADMIN' else '#3e3e3e'}; padding: 2px 8px; border-radius: 12px; font-size: 12px; margin-left: 8px;">{user['role']}</span>
                    <div style="color: #b3b3b3; font-size: 13px;">{user['email']}</div>
                </div>
                """, unsafe_allow_html=True)
            if user["userId"] != current_user_id():
                with col2:
                    if st.button("Delete", key=f"admin_del_{user['userId']}"):
                        api_delete(f"/users/{user['userId']}")
                        st.rerun()
    
    with tab2:
        log = api_get("/songs/activity-log")
        if not log:
            st.info("No activity logged yet.")
        for entry in reversed(log[-50:]):
            st.code(entry, language=None)

st.markdown('</div>', unsafe_allow_html=True)
render_now_playing()