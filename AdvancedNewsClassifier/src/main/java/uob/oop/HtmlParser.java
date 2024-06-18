package uob.oop;

public class HtmlParser {
    /***
     * Extract the title of the news from the _htmlCode.
     * @param _htmlCode Contains the full HTML string from a specific news. E.g. 01.htm.
     * @return Return the title if it's been found. Otherwise, return "Title not found!".
     */
    public static String getNewsTitle(String _htmlCode) {
        String titleTagOpen = "<title>";
        String titleTagClose = "</title>";

        int titleStart = _htmlCode.indexOf(titleTagOpen) + titleTagOpen.length();
        int titleEnd = _htmlCode.indexOf(titleTagClose);

        if (titleStart != -1 && titleEnd != -1 && titleEnd > titleStart) {
            String strFullTitle = _htmlCode.substring(titleStart, titleEnd);
            return strFullTitle.substring(0, strFullTitle.indexOf(" |"));
        }

        return "Title not found!";
    }

    /***
     * Extract the content of the news from the _htmlCode.
     * @param _htmlCode Contains the full HTML string from a specific news. E.g. 01.htm.
     * @return Return the content if it's been found. Otherwise, return "Content not found!".
     */
    public static String getNewsContent(String _htmlCode) {
        String contentTagOpen = "\"articleBody\": \"";
        String contentTagClose = " \",\"mainEntityOfPage\":";

        int contentStart = _htmlCode.indexOf(contentTagOpen) + contentTagOpen.length();
        int contentEnd = _htmlCode.indexOf(contentTagClose);

        if (contentStart != -1 && contentEnd != -1 && contentEnd > contentStart) {
            return _htmlCode.substring(contentStart, contentEnd).toLowerCase();
        }

        return "Content not found!";
    }

    public static NewsArticles.DataType getDataType(String _htmlCode) {
        //TODO Task 3.1 - 1.5 Marks
        String start = "<datatype>";
        String end = "</datatype>";
        int start_index = _htmlCode.indexOf(start) + start.length();
        int end_index = _htmlCode.indexOf(end);

        if (start_index != -1 && end_index != -1 && end_index > start_index) {
            String the_type = _htmlCode.substring(start_index, end_index).toLowerCase();
            if (the_type.equals("training")) {
                return NewsArticles.DataType.Training;
            }
            else{
                return NewsArticles.DataType.Testing;
            }
        }

        return NewsArticles.DataType.Testing; //Please modify the return value.
    }

    public static String getLabel (String _htmlCode) {
        //TODO Task 3.2 - 1.5 Marks
        String start = "<label>";
        String end = "</label>";
        int start_index = _htmlCode.indexOf(start) + start.length();
        int end_index = _htmlCode.indexOf(end);

        if (start_index != -1 && end_index != -1 && end_index > start_index) {
            return _htmlCode.substring(start_index, end_index);
        }
        return "-1"; //Please modify the return value.
    }


}
