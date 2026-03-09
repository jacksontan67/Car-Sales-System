package acss;

public interface Searchable {
    boolean matches(String criteria);
    String getSearchInfo();
}