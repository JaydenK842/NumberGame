import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/* Things to add:
    More operators
    Number range choice
    Better looking Panels
    Currency/Store?
    ...
 */

@SuppressWarnings("rawtypes")
public class Frame extends JFrame {
    //Scores
    double highScore = 0;
    double score = 0;
    double multiplier = 1;

    //Public font to use withing the frame
    Font font = new Font("Arial", Font.PLAIN, 75);

    //Countdown timer variables
    static int interval;
    int tChoice = 5;
    static Timer timer;
    JLabel countdown;
    final int delay = 1000;
    final int period = 1000;


    //Main frame
    JFrame frame;

    //Start and end screen
    JPanel startScreen;
    JPanel endScreen;
    JLabel title;
    JLabel eTitle;
    JButton start;
    JButton quit;
    JButton settings;

    //Settings screen
    JPanel settingsScreen;
    JLabel sTitle;
    JLabel buttons;
    JComboBox buttonAmount;
    JLabel interV;
    JComboBox intervalNum;
    JCheckBox timerActive;
    JCheckBox impossible;
    JButton save;
    JButton cancel;
    Object[] set = new Object[4];
    Object[] unSet = new Object[4];
    Boolean active = true;

    //Game screen and operator options
    JPanel game;
    String[] operators = new String[]{"+", "*", "-"};
    JLabel equation;
    JButton choice1 = new JButton();
    JButton choice2 = new JButton();
    JButton choice3 = new JButton();
    JButton choice4 = new JButton();
    JButton choice5 = new JButton();
    JButton choice6 = new JButton();

    //Buttons list and starting button amount
    JButton[] options = new JButton[]{choice1, choice2, choice3, choice4, choice5, choice6};
    int choiceAmount = 3;

    //Calculate a good-looking screen size
    Toolkit kit = Toolkit.getDefaultToolkit();
    Dimension screenSize = kit.getScreenSize();
    int screenHeight = screenSize.height;
    int screenWidth = screenSize.width;
    int frameHeight = screenHeight / 2;
    int frameWidth = screenWidth / 2;

    public Frame() {
        //Creates the frame and removes the layout
        frame = new JFrame();
        frame.setResizable(false);
        frame.setLayout(null);

        //Frame title
        frame.setTitle("Blitz");

        //Sets the proper frame size and location
        frame.setSize(frameWidth, frameHeight);
        frame.setLocation(screenWidth / 4, screenHeight / 4);

        //Creates the start screen, sets its location and adds it to the frame
        openScreen();
        startScreen.setBounds(0, 0, frameWidth - 16, frameHeight - 39);
        frame.add(startScreen);

        //Creates start screen, sets its location and adds it to the frame
        settings();
        settingsScreen.setBounds(0, 0, frameWidth - 16, frameHeight - 39);
        settingsScreen.setVisible(false);
        frame.add(settingsScreen);

        //Creates the game screen, sets its location and adds it to the frame
        gameScreen();
        game.setBounds(0, 0, frameWidth - 16, frameHeight - 39);
        game.setVisible(false);
        frame.add(game);

        //Creates the end screen, sets its location and adds it to the frame
        closeScreen();
        endScreen.setBounds(0, 0, frameWidth - 16, frameHeight - 39);
        endScreen.setVisible(false);
        frame.add(endScreen);

        //Colors the background of the screens and sets the frame to be visible
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.LIGHT_GRAY);
        startScreen.setBackground(Color.LIGHT_GRAY);
        game.setBackground(new Color(9,136,186));
        endScreen.setBackground(Color.LIGHT_GRAY);
        frame.setVisible(true);
    }

    //Open screen initializer
    private void openScreen() {
        //Creates the start screen and removes the layout
        startScreen = new JPanel();
        startScreen.setLayout(null);

        //Creates the main title, designs and then organizes it within the frame
        title = new JLabel("Blitz");
        title.setFont(font);
        title.setBounds((frameWidth - getTextSize(title.getText(), 75)) / 2, 50, getTextSize(title.getText(), 75), 63);
        startScreen.add(title);

        //Creates the button that allows to begin the game
        start = new JButton("Play");
        start.setFocusable(false);
        start.addActionListener((ActionEvent ae) -> begin(1));
        start.setFont(new Font("Arial", Font.PLAIN, 45));
        start.setBorder(new BevelBorder(BevelBorder.LOWERED));
        start.setBounds((frameWidth - 200) / 4, frameHeight - 289, 200, 75);
        startScreen.add(start);

        //Creates a settings button
        settings = new JButton("Settings");
        settings.setFocusable(false);
        settings.addActionListener((ActionEvent ae) -> begin(2));
        settings.setFont(new Font("Arial", Font.PLAIN, 45));
        settings.setBorder(new BevelBorder(BevelBorder.LOWERED));
        settings.setBounds(((frameWidth- 200) * 3) / 4, frameHeight - 289, 200, 75);
        startScreen.add(settings);

        //Creates a quit button to leave the game
        quit = new JButton("Exit");
        quit.setFocusable(false);
        quit.addActionListener((ActionEvent ae) -> quit());
        quit.setFont(new Font("Arial", Font.PLAIN, 45));
        quit.setBorder(new BevelBorder(BevelBorder.LOWERED));
        quit.setBounds((frameWidth - 200) / 2, frameHeight - 175, 200, 75);
        startScreen.add(quit);
    }

    //Game screen initializer
    private void gameScreen() {
        //Creates the game panel and removes the layout
        game = new JPanel();
        game.setLayout(null);

        //Adds and equation label that will display a random math equation
        equation = new JLabel();
        equation.setFont(font);
        equation.setBounds((frameWidth - 270) / 2,50,270,63);
        game.add(equation);

        //Adds a countdown timer
        countdown = new JLabel("5");
        countdown.setFont(font);
        countdown.setBounds((frameWidth - 100), 5, 90, 63);
        game.add(countdown);

        //Set the button locations
        choice1.setBounds((frameWidth - 200) / 7, frameHeight - 289, 200, 75);
        choice2.setBounds((frameWidth - 200) / 2, frameHeight - 289, 200, 75);
        choice3.setBounds(((frameWidth - 200) / 7) * 6, frameHeight - 289, 200, 75);
        choice4.setBounds((frameWidth - 200) / 7, frameHeight - 189, 200, 75);
        choice4.setVisible(false);
        choice5.setBounds((frameWidth - 200) / 2, frameHeight - 189, 200, 75);
        choice5.setVisible(false);
        choice6.setBounds(((frameWidth - 200) / 7) * 6, frameHeight - 189, 200, 75);
        choice6.setVisible(false);


        //Makes each of the buttons look well
        for (JButton x : options) {
            x.setFocusable(false);
            x.addActionListener((ActionEvent ae) -> update(x.getText(), equation.getText()));
            x.setFont(new Font("Arial", Font.PLAIN, 45));
            game.add(x);
        }
    }

    //Close screen initializer
    private void closeScreen() {
        //Creates an end screen and removes the layout
        endScreen = new JPanel();
        endScreen.setLayout(null);

        //Creates a game over title
        eTitle = new JLabel("Game Over");
        eTitle.setFont(font);
        eTitle.setBounds((frameWidth - getTextSize(eTitle.getText(), 75)) / 2, 50, getTextSize(eTitle.getText(), 75), 63);
        endScreen.add(eTitle);

        //Creates a try again button
        start = new JButton("Try Again");
        start.setFocusable(false);
        start.addActionListener((ActionEvent ae) -> begin(1));
        start.setFont(new Font("Arial", Font.PLAIN, 45));
        start.setBorder(new BevelBorder(BevelBorder.LOWERED));
        start.setBounds((frameWidth - 200) / 4, frameHeight - 289, 200, 75);
        endScreen.add(start);

        //Settings screen button
        settings = new JButton("Settings");
        settings.setFocusable(false);
        settings.addActionListener((ActionEvent ae) -> begin(2));
        settings.setFont(new Font("Arial", Font.PLAIN, 45));
        settings.setBorder(new BevelBorder(BevelBorder.LOWERED));
        settings.setBounds(((frameWidth- 200) * 3) / 4, frameHeight - 289, 200, 75);
        endScreen.add(settings);

        //Creates and exit button
        quit = new JButton("Exit");
        quit.setFocusable(false);
        quit.addActionListener((ActionEvent ae) -> quit());
        quit.setFont(new Font("Arial", Font.PLAIN, 45));
        quit.setBorder(new BevelBorder(BevelBorder.LOWERED));
        quit.setBounds((frameWidth - 200) / 2, frameHeight - 175, 200, 75);
        endScreen.add(quit);
    }

    //Settings menu initializer
    private void settings() {
        //Settings screen panel
        settingsScreen = new JPanel();
        settingsScreen.setLayout(null);

        //Settings title
        sTitle = new JLabel("Settings");
        sTitle.setFont(font);
        sTitle.setBounds((frameWidth - getTextSize(sTitle.getText(), 75)) / 2, 50, getTextSize(sTitle.getText(), 75), 82);
        settingsScreen.add(sTitle);

        //Button count label
        buttons = new JLabel("Button Count");
        buttons.setFont(new Font("Arial", Font.PLAIN, 15));
        buttons.setBounds(((frameWidth - getTextSize(buttons.getText(), 15)) * 9) / 16, frameHeight - 375, getTextSize(buttons.getText(), 15), 20);
        settingsScreen.add(buttons);

        //Combo box that allows the user to choose how many buttons there are
        buttonAmount = new JComboBox(new String[]{"3", "4", "5", "6"});
        buttonAmount.setFont(new Font("Arial", Font.PLAIN, 15));
        buttonAmount.setBounds((((frameWidth - 45) * 9) / 16) + getTextSize(buttons.getText(), 15), frameHeight - 375, 45, 20);
        settingsScreen.add(buttonAmount);
        set[0] = buttonAmount;
        unSet[0] = buttonAmount.getSelectedIndex();

        //The impossible button...
        impossible = new JCheckBox("Impossible");
        impossible.setFont(new Font("Arial", Font.PLAIN, 15));
        impossible.setBounds(((frameWidth - getTextSize(impossible.getText(), 15) + 25) * 3) / 8, frameHeight - 375, getTextSize(impossible.getText(), 15) + 25, 20);
        impossible.setFocusable(false);
        settingsScreen.add(impossible);
        set[3] = impossible;
        unSet[3] = impossible.isSelected();

        //Time limit label
        interV = new JLabel("Time Limit");
        interV.setFont(new Font("Arial", Font.PLAIN, 15));
        interV.setBounds(((frameWidth - getTextSize(buttons.getText(), 15)) * 9) / 16, frameHeight - 335, getTextSize(buttons.getText(), 15), 20);
        settingsScreen.add(interV);

        //Combo box that allows the user to choose a time limit
        intervalNum = new JComboBox(new String[]{"3", "4", "5", "6", "7", "8", "9", "10"});
        intervalNum.setFont(new Font("Arial", Font.PLAIN, 15));
        intervalNum.setBounds((((frameWidth - 45) * 9) / 16) + getTextSize(interV.getText(), 15), frameHeight - 335, 45, 20);
        intervalNum.setSelectedIndex(2);
        settingsScreen.add(intervalNum);
        set[1] = intervalNum;
        unSet[1] = intervalNum.getSelectedIndex();

        //Allows the user to turn the timer on or off
        timerActive = new JCheckBox("Timer Active");
        timerActive.setFont(new Font("Arial", Font.PLAIN, 15));
        timerActive.setBounds(impossible.getX(), frameHeight - 335, getTextSize(timerActive.getText(), 15) + 25, 20);
        timerActive.setSelected(true);
        timerActive.setFocusable(false);
        settingsScreen.add(timerActive);
        timerActive.addActionListener((ActionEvent ae) -> {if (interV.isEnabled()) {interV.setEnabled(false); intervalNum.setEnabled(false);} else {interV.setEnabled(true); intervalNum.setEnabled(true);}});
        set[2] = timerActive;
        unSet[2] = timerActive.isSelected();

        //Button that allows the user to save the settings they chose
        save = new JButton("Save");
        save.setFocusable(false);
        save.addActionListener((ActionEvent ae) -> SS(set));
        save.setFont(new Font("Arial", Font.PLAIN, 45));
        save.setBorder(new BevelBorder(BevelBorder.LOWERED));
        save.setBounds((frameWidth - 200) / 4, frameHeight - 175, 200, 75);
        settingsScreen.add(save);

        //Button that allows the user to cancel their settings change
        cancel = new JButton("Cancel");
        cancel.setFocusable(false);
        cancel.addActionListener((ActionEvent ae) -> reset(unSet));
        cancel.setFont(new Font("Arial", Font.PLAIN, 45));
        cancel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        cancel.setBounds(((frameWidth - 200) * 3) / 4, frameHeight - 175, 200, 75);
        settingsScreen.add(cancel);

        //Enables and disables other controls depending on if the impossible button is checked or not (down here so everything can be initialized first)
        impossible.addActionListener((ActionEvent ae) -> {if (buttons.isEnabled()) {timerActive.setEnabled(false); buttons.setEnabled(false); buttonAmount.setEnabled(false);} else {timerActive.setEnabled(true); buttons.setEnabled(true); buttonAmount.setEnabled(true);} if (!buttons.isEnabled()) {interV.setEnabled(false); intervalNum.setEnabled(false);} else if (timerActive.isSelected()) {interV.setEnabled(true); intervalNum.setEnabled(true);}});
    }

    private void SS(Object[] set) {
        //Impossible button
        JCheckBox imp = (JCheckBox) set[3];
        unSet[3] = imp.isSelected();

        JComboBox x = (JComboBox) set[0];
        JComboBox z = (JComboBox) set[1];
        JCheckBox f = (JCheckBox) set[2];
        if (!imp.isSelected()) {
            //Saves the amount of buttons selected
            for (int i = 0; i < options.length; i++) {
                JButton y = options[i];

                //Turns on all the buttons that are selected in the settings
                y.setVisible(i < Integer.parseInt((String) Objects.requireNonNull(x.getSelectedItem())));
            }

            //Activates or deactivates the timer
            active = f.isSelected();

            //Sets the timer length according to the users selection
            tChoice = Integer.parseInt((String) Objects.requireNonNull(z.getSelectedItem()));
        } else {
            //Sets 5 buttons active for the impossible difficulty
            for (int i = 0; i < 5; i++) {
                JButton y = options[i];
                y.setVisible(true);
            }

            //Activates the timer with 5 seconds
            active = true;
            tChoice = 5;
        }

        //Updates unSet (I honestly forgot what this does...)
        unSet[0] = x.getSelectedIndex();
        unSet[1] = z.getSelectedIndex();
        unSet[2] = f.isSelected();

        //Sends you to the main menu
        begin(3);
    }

    private void reset(Object[] unset) {
        //Makes the button enabling and disabling work
        boolean done = false;

        //Resets the button number selection
        JComboBox x = (JComboBox) set[0];
        int y = (int) unset[0];
        x.setSelectedIndex(y);

        //Resets the timer length choice
        x = (JComboBox) set[1];
        y = (int) unset[1];
        x.setSelectedIndex(y);

        //Resets the timer active check box
        JCheckBox a = (JCheckBox) set[2];
        Boolean z = (Boolean) unset[2];

        //Resets the timer active enabling
        if (a.isSelected() != z) {
            if (interV.isEnabled()) {interV.setEnabled(false); intervalNum.setEnabled(false);} else {interV.setEnabled(true); intervalNum.setEnabled(true);}
            done = true;
        }
        a.setSelected(z);

        //Resets the impossible button enabling
        a = (JCheckBox) set[3];
        z = (Boolean) unset[3];
        if (a.isSelected() != z) {
            if (interV.isEnabled() && !done) {interV.setEnabled(false); intervalNum.setEnabled(false);} else if (timerActive.isSelected() && !done) {interV.setEnabled(true); intervalNum.setEnabled(true);}
            timerActive.setEnabled(!timerActive.isEnabled());
            if (buttons.isEnabled()) {buttons.setEnabled(false); buttonAmount.setEnabled(false);} else {buttons.setEnabled(true); buttonAmount.setEnabled(true);}
        }
        a.setSelected(z);

        //Sends the user back to the main menu afterward
        begin(3);
    }

    //Updates the screen for the next level
    private void update(String[] operators, JLabel equation, JButton[] options) {
        //Initial variables
        boolean givenCorrect = false;
        int i = 1;

        //Creates a random equation: (1-20) (*,+,-) (1-20)
        int num1 = (int)(Math.random() * 20), num2 = (int)(Math.random() * 20);
        String operand = operators[(int)(Math.random() * 3)];
        String full = num1 + " " + operand +  " " + num2;
        equation.setText(full);

        //Changes the text box size depending on the length of the equation
        equation.setBounds((frameWidth - getTextSize(full, 75)) / 2, 50, getTextSize(full, 75), 63);

        //If its in impossible mode, it shifts all the buttons and the equation
        //Otherwise, it resets the location of all buttons and the equation
        JCheckBox f = (JCheckBox) set[3];
        if (f.isSelected()) {
            equation.setLocation((int)(Math.random() * ((frameWidth - equation.getWidth())+ 1)), (int)(Math.random() * ((frameHeight - equation.getHeight())+ 1)));
            for (JButton q : options) {
                q.setLocation((int)(Math.random() * ((frameWidth - q.getWidth())+ 1)), (int)(Math.random() * ((frameHeight - q.getHeight())+ 1)));
                while (true) {
                    if (q.getBounds().intersects(equation.getBounds())) {
                        q.setLocation((int)(Math.random() * ((frameWidth - q.getWidth())+ 1)), (int)(Math.random() * ((frameHeight - q.getHeight())+ 1)));
                    }  else {
                        break;
                    }
                }
            }
        } else {
            choice1.setBounds((frameWidth - 200) / 7, frameHeight - 289, 200, 75);
            choice2.setBounds((frameWidth - 200) / 2, frameHeight - 289, 200, 75);
            choice3.setBounds(((frameWidth - 200) / 7) * 6, frameHeight - 289, 200, 75);
            choice4.setBounds((frameWidth - 200) / 7, frameHeight - 189, 200, 75);
            choice5.setBounds((frameWidth - 200) / 2, frameHeight - 189, 200, 75);
            choice6.setBounds(((frameWidth - 200) / 7) * 6, frameHeight - 189, 200, 75);
        }

        //Calculates to find the correct answer
        int correct = calculate(full);

        //Randomizes the buttons' text
        for (JButton x : options) {
            if (x.isVisible()) {
                //Ensures there is only one option that has the correct answer
                if (!givenCorrect) {
                    //Cheap little randomizer but if it's the last button, it ensures it to be the answer
                    if (((int) (Math.random() * 10) > 5) || (choiceAmount == i)) {
                        x.setText(String.valueOf(correct));
                        givenCorrect = true;
                        continue;
                    }
                }

                //Gives the button a random number that is 1-30 away from the correct answer
                while (true) {
                    x.setText(String.valueOf((int) (Math.random() * ((correct + 30) - (correct - 30)) + (correct - 30))));
                    if (x.getText().equals(String.valueOf(correct))) {
                        continue;
                    }
                    break;
                }

                //Increment to guarantee one button has the correct answer
                i++;
            }
        }

        if (active) {
            //Resets/Sets the countdown number
            countdown.setText(String.valueOf(tChoice));

            //Tries to cancel a timer unless it is the first time its being set
            try {
                timer.cancel();
            } catch (NullPointerException e) {
                //e.printStackTrace();
            }

            //Schedules a fixed rate timer
            timer = new Timer();
            interval = tChoice;
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    countdown.setText(String.valueOf(setInterval()));
                }
            }, delay, period);
        } else {
            countdown.setText("");
        }
    }

    private int setInterval() {
        if (interval == 1) {
            timer.cancel();
            //CHANGE ACTION TO LET THEM KNOW THEY RAN OUT OF TIME
            begin(0);
        }

        return --interval;
    }

    //Overloads the other update to check if they chose the correct answer
    private void update(String guess, String answer) {
        //Calculates the true answer
        int value = calculate(answer);

        //If it's correct, updates to the next problem
        if (Integer.parseInt(guess) == value) {
            score++;
            update(operators, equation, options);
        } else {
            try {
                timer.cancel();
            } catch (NullPointerException e) {
                //e.printStackTrace();
            }

            if (active) {
                switch (tChoice) {
                    case 3 -> multiplier += 0.2;
                    case 4 -> multiplier += 0.1;
                }
            }

            int i = 0;
            for (JButton x : options) {
                if (x.isVisible()) {
                    if (i >= 3) {
                        multiplier += 0.1;
                    }
                    i++;
                }
            }

            JCheckBox a = (JCheckBox) set[3];
            if (a.isSelected()) {
                multiplier += 1.2;
            }

            begin(0);
        }
    }

    //Menu changing method
    private void begin(int action) {
        //To game screen
        switch (action) {
            case 1 -> {
                if (startScreen.isVisible()) {
                    startScreen.setVisible(false);
                } else {
                    endScreen.setVisible(false);
                }

                update(operators, equation, options);
                game.setVisible(true);
            }

            //Settings screen
            case 2 -> {
                if (startScreen.isVisible()) {
                    startScreen.setVisible(false);
                } else {
                    endScreen.setVisible(false);
                }

                settingsScreen.setVisible(true);
            }
            case 3 -> {
                settingsScreen.setVisible(false);
                startScreen.setVisible(true);
            }

            //End screen
            default -> {
                game.setVisible(false);
                endScreen.setVisible(true);

                score = (score * multiplier) * 100;

                if (score > highScore) {
                    highScore = score;
                }

                //Shows the user their score and highscore
                JOptionPane.showMessageDialog(endScreen, "Highscore:" + (int) highScore + "\nScore: " + (int) score);
                score = 0;
                multiplier = 1;
            }
        }
    }

    //Calculates the answer of an equation from type(String)
    public int calculate(String equation) {
        //Removes spacing in the equation and declares variables
        equation = equation.replaceAll("\\s", "");
        int i = 0, firstNum = 0, secondNum, calc = 0;
        StringBuilder x = new StringBuilder();
        String op = "";

        //Splices the equation so that it can be calculated
        while (true) {
            try {
                //Test Case to see if it's at the operator of the equation
                Integer.valueOf(equation.substring(i, i+1));

                //Adds the number onto the last added number and increments
                x.append(equation.charAt(i));
                i++;

            //If it's at the operator, it stores the first number and operator, then resets for the second number
            } catch (NumberFormatException e) {
                firstNum = Integer.parseInt(x.toString());

                x = new StringBuilder();
                op = equation.substring(i, i+1);
                i++;

            //When it's out of bounds, it stores the second number
            } catch (IndexOutOfBoundsException e) {
                secondNum = Integer.parseInt(x.toString());
                break;
            }
        }

        //Checks the operator of the equation and calculates the result
        switch (op) {
            case "+" -> calc = firstNum + secondNum;
            case "*" -> calc = firstNum * secondNum;
            case "-" -> calc = firstNum - secondNum;
        }

        //Returns the calculation
        return calc;
    }

    //Calculates the width of the text given; used for sizing to fit text
    private int getTextSize(String text, int size) {
        FontMetrics metrics = new FontMetrics(new Font("Arial", Font.PLAIN, size)) {};
        Rectangle2D bounds = metrics.getStringBounds(text, null);

        return (int) bounds.getWidth();
    }

    //Method for the quit button to system exit
    private void quit() {
        System.exit(0);
    }
}