package bootcamp.bowling;


import java.util.ArrayList;
import java.util.List;

public class Frame {


    private List<String> rolls = new ArrayList<>();
    private int score;

    public List<String> getRolls() {
        return rolls;
    }

    public void setRolls(List<String> rolls) {
        this.rolls = rolls;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
