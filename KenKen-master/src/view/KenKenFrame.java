package view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import kenken.Board;

public class KenKenFrame {

    private final JFrame frame = new JFrame("KenKen - ケンケン");
    private KenKenGrid grid;

    public int prob1;
    public int prob2;
    public int prob4;

    public int probSum;
    public int probSub;
    public int probDiv;
    public int probMod;
    public int probMult;
    public int threadAmount;

    public KenKenFrame() {
        loadSettings();
        frame.getContentPane().add(grid = new KenKenGrid(5, prob1, prob2, prob4, probSum, probSub, probDiv, probMult, probMod, threadAmount));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildMenu();
        frame.pack();
        frame.setResizable(false);
        centerView();
        frame.setVisible(true);
    }

    public void readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded;
        Gson gson = new Gson();
        encoded = Files.readAllBytes(Paths.get(path));
        Board game = (Board) gson.fromJson(new String(encoded, encoding), Board.class);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(grid = new KenKenGrid(game,threadAmount));
        frame.pack();
        centerView();

    }

    private void buildMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem open = new JMenuItem("Open");
        JMenuItem settings = new JMenuItem("Settings");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem grid5 = new JMenuItem("5x5");
        JMenuItem grid6 = new JMenuItem("6x6");
        JMenuItem grid7 = new JMenuItem("7x7");
        JMenuItem grid8 = new JMenuItem("8x8");
        JMenuItem grid9 = new JMenuItem("9x9");
        JMenuItem grid10 = new JMenuItem("10x10");
        JMenuItem grid11 = new JMenuItem("11x11");
        JMenuItem grid12 = new JMenuItem("12x12");
        JMenuItem grid13 = new JMenuItem("13x13");
        JMenuItem grid14 = new JMenuItem("14x14");
        JMenuItem grid15 = new JMenuItem("15x15");
        JMenuItem grid16 = new JMenuItem("16x16");
        JMenuItem grid17 = new JMenuItem("17x17");
        JMenuItem grid18 = new JMenuItem("18x18");
        JMenuItem grid19 = new JMenuItem("19x19");

        JMenuItem about = new JMenuItem("About");
        JMenu sizes = new JMenu("Size");
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.addSeparator();
        sizes.add(grid5);
        sizes.add(grid6);
        sizes.add(grid7);
        sizes.add(grid8);
        sizes.add(grid9);
        sizes.add(grid10);
        sizes.add(grid11);
        sizes.add(grid12);
        sizes.add(grid13);
        sizes.add(grid14);
        sizes.add(grid15);
        sizes.add(grid16);
        sizes.add(grid17);
        sizes.add(grid18);
        sizes.add(grid19);
        fileMenu.add(sizes);
        fileMenu.addSeparator();
        fileMenu.add(about);
        fileMenu.add(settings);

        bar.add(fileMenu);

        open.addActionListener((ActionEvent e) -> {
            JFileChooser chooser = new JFileChooser();
            int status = chooser.showOpenDialog(frame);

            if (status == JFileChooser.APPROVE_OPTION) {
                String filename = chooser.getSelectedFile().getPath();
                try {
                    readFile(filename, Charset.defaultCharset());
                } catch (IOException ex) {
                    Logger.getLogger(KenKenFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        save.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                try {
                    grid.initialGameBoard.saveBoard(fileChooser.getSelectedFile().getPath());
                } catch (IOException ex) {
                    Logger.getLogger(KenKenFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        settings.addActionListener((ActionEvent e) -> {
            new Settings(this).setVisible(true);

        });

        grid5.addActionListener((ActionEvent e) -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(grid = new KenKenGrid(5, prob1, prob2, prob4, probSum, probSub, probDiv, probMult, probMod, threadAmount));
            frame.pack();
            centerView();
        });
        grid6.addActionListener((ActionEvent e) -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(grid = new KenKenGrid(6, prob1, prob2, prob4, probSum, probSub, probDiv, probMult, probMod, threadAmount));
            frame.pack();
            centerView();
        });
        grid7.addActionListener((ActionEvent e) -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(grid = new KenKenGrid(7, prob1, prob2, prob4, probSum, probSub, probDiv, probMult, probMod, threadAmount));
            frame.pack();
            centerView();
        });
        grid8.addActionListener((ActionEvent e) -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(grid = new KenKenGrid(8, prob1, prob2, prob4, probSum, probSub, probDiv, probMult, probMod, threadAmount));
            frame.pack();
            centerView();
        });
        grid9.addActionListener((ActionEvent e) -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(grid = new KenKenGrid(9, prob1, prob2, prob4, probSum, probSub, probDiv, probMult, probMod, threadAmount));
            frame.pack();
            centerView();
        });
        grid10.addActionListener((ActionEvent e) -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(grid = new KenKenGrid(10, prob1, prob2, prob4, probSum, probSub, probDiv, probMult, probMod, threadAmount));
            frame.pack();
            centerView();
        });
        grid11.addActionListener((ActionEvent e) -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(grid = new KenKenGrid(11, prob1, prob2, prob4, probSum, probSub, probDiv, probMult, probMod, threadAmount));
            frame.pack();
            centerView();
        });
        grid12.addActionListener((ActionEvent e) -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(grid = new KenKenGrid(12, prob1, prob2, prob4, probSum, probSub, probDiv, probMult, probMod, threadAmount));
            frame.pack();
            centerView();
        });
        grid13.addActionListener((ActionEvent e) -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(grid = new KenKenGrid(13, prob1, prob2, prob4, probSum, probSub, probDiv, probMult, probMod, threadAmount));
            frame.pack();
            centerView();
        });
        grid14.addActionListener((ActionEvent e) -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(grid = new KenKenGrid(14, prob1, prob2, prob4, probSum, probSub, probDiv, probMult, probMod, threadAmount));
            frame.pack();
            centerView();
        });
        grid15.addActionListener((ActionEvent e) -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(grid = new KenKenGrid(15, prob1, prob2, prob4, probSum, probSub, probDiv, probMult, probMod, threadAmount));
            frame.pack();
            centerView();
        });

        grid16.addActionListener((ActionEvent e) -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(grid = new KenKenGrid(16, prob1, prob2, prob4, probSum, probSub, probDiv, probMult, probMod, threadAmount));
            frame.pack();
            centerView();
        });
        grid17.addActionListener((ActionEvent e) -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(grid = new KenKenGrid(17, prob1, prob2, prob4, probSum, probSub, probDiv, probMult, probMod, threadAmount));
            frame.pack();
            centerView();
        });
        grid18.addActionListener((ActionEvent e) -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(grid = new KenKenGrid(18, prob1, prob2, prob4, probSum, probSub, probDiv, probMult, probMod, threadAmount));
            frame.pack();
            centerView();
        });
        grid19.addActionListener((ActionEvent e) -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(grid = new KenKenGrid(19, prob1, prob2, prob4, probSum, probSub, probDiv, probMult, probMod, threadAmount));
            frame.pack();
            centerView();
        });
        about.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(
                    null,
                    "By Angelo Ramírez, Giulliano D´Ambrosio. April, 2018",
                    "About",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        frame.setJMenuBar(bar);
    }

    public ArrayList<Integer> fromJsonToSettingsArray(String json) {
        Gson gson = new Gson();
        return (ArrayList<Integer>) gson.fromJson(json, new TypeToken<List<Integer>>() {
        }.getType());
    }

    private void loadSettings() {
        byte[] encoded = new byte[2];

        File f = new File("gameSettings.dat");
        if (f.exists()) {
            try {
                encoded = Files.readAllBytes(Paths.get("gameSettings.dat"));
            } catch (IOException ex) {
                Logger.getLogger(KenKenFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            ArrayList<Integer> settings = fromJsonToSettingsArray(new String(encoded, Charset.defaultCharset()));
            this.prob1 = settings.get(0);
            this.prob2 = settings.get(1);
            this.prob4 = settings.get(2);
            this.probSum = settings.get(3);
            this.probSub = settings.get(4);
            this.probDiv = settings.get(5);
            this.probMult = settings.get(6);
            this.probMod = settings.get(7);
            this.threadAmount = settings.get(8);
        } else {
            loadDefaultSettings();
        }

    }

    public void saveSettings(ArrayList<Integer> settings) {
        try {
            this.prob1 = settings.get(0);
            this.prob2 = settings.get(1);
            this.prob4 = settings.get(2);
            this.probSum = settings.get(3);
            this.probSub = settings.get(4);
            this.probDiv = settings.get(5);
            this.probMult = settings.get(6);
            this.probMod = settings.get(7);
            this.threadAmount = settings.get(8);
            Gson gson = new Gson();
            BufferedWriter writer;
            writer = new BufferedWriter(new FileWriter("gameSettings.dat"));
            writer.write(gson.toJson(settings));
            List<String> lines = Arrays.asList(gson.toJson(settings));
            Path file = Paths.get("gameSettings.dat");
            Files.write(file, lines, Charset.forName("UTF-8"));

            frame.getContentPane().removeAll();
            frame.getContentPane().add(grid = new KenKenGrid(grid.initialGameBoard.size, prob1, prob2, prob4, probSum, probSub, probDiv, probMult, probMod, threadAmount));
            frame.pack();
        } catch (IOException ex) {
            Logger.getLogger(KenKenFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadDefaultSettings() {
        this.prob1 = 20;
        this.prob2 = 40;
        this.prob4 = 40;
        this.probSum = 5;
        this.probSub = 5;
        this.probDiv = 35;
        this.probMult = 35;
        this.probMod = 20;
        this.threadAmount = 1;
    }

    private void centerView() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();

        frame.setLocation((screen.width - frameSize.width) >> 1,
                (screen.height - frameSize.height) >> 1);
    }

}
