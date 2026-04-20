package com.musicapp;

import com.musicapp.model.Admin;
import com.musicapp.model.Artist;
import com.musicapp.model.Song;
import com.musicapp.model.Album;
import com.musicapp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class MusicAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicAppApplication.class, args);
    }

    /**
     * Seeds in-memory data on startup so the UI has something to display immediately.
     */
    @Bean
    CommandLineRunner seedData(UserRepository userRepo,
                               ArtistRepository artistRepo,
                               AlbumRepository albumRepo,
                               SongRepository songRepo) {
        return args -> {
            // --- Seed Admin ---
            Admin admin = new Admin(1, "Admin", "admin@music.com", "admin123", "ADMIN");
            userRepo.save(admin);

            // --- Seed Artists ---
            Artist a1 = new Artist(1, "The Weeknd", "Canadian singer-songwriter and record producer.");
            Artist a2 = new Artist(2, "Taylor Swift", "American singer-songwriter known for narrative songwriting.");
            Artist a3 = new Artist(3, "Ed Sheeran", "English singer-songwriter known for acoustic pop.");
            artistRepo.save(a1);
            artistRepo.save(a2);
            artistRepo.save(a3);

            // --- Seed Songs ---
            Song s1 = new Song(1, "Blinding Lights", 200, "Synth-pop", "files/blinding_lights.mp3");
            Song s2 = new Song(2, "Starboy", 230, "R&B", "files/starboy.mp3");
            Song s3 = new Song(3, "Anti-Hero", 193, "Pop", "files/anti_hero.mp3");
            Song s4 = new Song(4, "Shake It Off", 219, "Pop", "files/shake_it_off.mp3");
            Song s5 = new Song(5, "Shape of You", 234, "Pop", "files/shape_of_you.mp3");
            Song s6 = new Song(6, "Perfect", 263, "Pop ballad", "files/perfect.mp3");
            songRepo.save(s1); songRepo.save(s2); songRepo.save(s3);
            songRepo.save(s4); songRepo.save(s5); songRepo.save(s6);

            // Link songs to artists
            a1.uploadSong(s1); a1.uploadSong(s2);
            a2.uploadSong(s3); a2.uploadSong(s4);
            a3.uploadSong(s5); a3.uploadSong(s6);

            // --- Seed Albums ---
            Album al1 = new Album(1, "After Hours", LocalDate.of(2020, 3, 20));
            al1.addSong(s1); al1.addSong(s2);
            a1.createAlbum(al1);

            Album al2 = new Album(2, "Midnights", LocalDate.of(2022, 10, 21));
            al2.addSong(s3); al2.addSong(s4);
            a2.createAlbum(al2);

            Album al3 = new Album(3, "Divide", LocalDate.of(2017, 3, 3));
            al3.addSong(s5); al3.addSong(s6);
            a3.createAlbum(al3);

            albumRepo.save(al1); albumRepo.save(al2); albumRepo.save(al3);

            System.out.println("✅  Music Management System started — seed data loaded.");
            System.out.println("   API available at: http://localhost:8080/api");
        };
    }
}
