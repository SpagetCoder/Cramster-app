package zientek.lukasz.learnwords.model;

public class TestQuestions
{
    private String word;
    private String correctTranslation;

    public TestQuestions(String word, String correctTranslation)
    {
        this.word = word;
        this.correctTranslation = correctTranslation;
    }

    public String getWord()
    {
        return word;
    }

    public void setWord(String word)
    {
        this.word = word;
    }

    public String getCorrectTranslation()
    {
        return correctTranslation;
    }

    public void setCorrectTranslation(String correctTranslation)
    {
        this.correctTranslation = correctTranslation;
    }
}
