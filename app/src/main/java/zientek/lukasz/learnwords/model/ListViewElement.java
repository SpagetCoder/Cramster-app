package zientek.lukasz.learnwords.model;

public class ListViewElement
{
    private String testName;

    public ListViewElement(Object testName)
    {
        this.testName = (String) testName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

}


