import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleMusicPlayer extends JFrame {
    private JButton playButton, pauseButton, stopButton, shuffleButton;
    private JList<String> playlist;
    private DefaultListModel<String> playlistModel;
    private Clip clip;
    private List<String> songs;
    private boolean isPlaying = false;

    public SimpleMusicPlayer() {
        setTitle("Sangeetak");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Initialize components
        playlistModel = new DefaultListModel<>();
        playlist = new JList<>(playlistModel);
        playlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        playButton = new JButton("Play");
        pauseButton = new JButton("Pause");
        stopButton = new JButton("Stop");
        shuffleButton = new JButton("Shuffle");

        // Layout
        setLayout(new BorderLayout());
        add(new JScrollPane(playlist), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(stopButton);
        controlPanel.add(shuffleButton);
        add(controlPanel, BorderLayout.SOUTH);

        // Initialize song list (change these paths to your own MP3 files)
        songs = new ArrayList<>();
        songs.add("./Assests/party.wav");
        songs.add("./Assests/Badtameez dil trimmed.wav");
        // songs.add("path/to/your/song3.mp3");

        File file = new File(songs.get(0));
        System.out.println("Absolute Path: " + file.getAbsolutePath());

        // Populate playlist
        for (String song : songs) {
            playlistModel.addElement(new File(song).getName());
        }

        // Event listeners
        playlist.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                stop();
                playSelectedSong();
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPlaying) {
                    pause();
                } else {
                    play();
                }
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pause();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });

        shuffleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shufflePlaylist();
            }
        });
    }

    private void playSelectedSong() {
        int selectedIndex = playlist.getSelectedIndex();
        if (selectedIndex != -1) {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(songs.get(selectedIndex)));
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
                isPlaying = true;
                playButton.setText("Pause");
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void play() {
        if (clip != null && !clip.isRunning()) {
            clip.start();
            isPlaying = true;
            playButton.setText("Pause");
        }
    }

    private void pause() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            isPlaying = false;
            playButton.setText("Play");
        }
    }

    private void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
            isPlaying = false;
            playButton.setText("Play");
        }
    }

    private void shufflePlaylist() {
        Collections.shuffle(songs);
        playlistModel.clear();
        for (String song : songs) {
            playlistModel.addElement(new File(song).getName());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimpleMusicPlayer().setVisible(true);
            }
        });
    }
}