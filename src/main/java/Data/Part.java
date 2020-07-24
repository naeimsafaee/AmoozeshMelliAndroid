package Data;

import com.google.gson.annotations.SerializedName;

public class Part {

    @SerializedName("id")
    private int ID;

    @SerializedName("order")
    private int order;

    private boolean is_video;

    @SerializedName("has_ended")
    private boolean has_ended;

    @SerializedName("quiz")
    private Quiz1 quiz;

    private Video video;
    private QuestionPart questionPart;

    public QuestionPart getQuestionPart() {
        return questionPart;
    }

    public void setQuestionPart(QuestionPart questionPart) {
        this.questionPart = questionPart;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getID() {
        return ID;
    }

    public int getOrder() {
        return order;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public boolean isIs_video() {
        return is_video;
    }

    public void setIs_video(boolean is_video) {
        this.is_video = is_video;
    }

    public boolean isHas_ended() {
        return has_ended;
    }

    public Quiz1 getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz1 quiz) {
        this.quiz = quiz;
    }

    public void setHas_ended(boolean has_ended) {
        this.has_ended = has_ended;
    }
}
