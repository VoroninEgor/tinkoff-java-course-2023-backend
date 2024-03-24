package edu.java.dto.github;

public record GitHubCommitResponse(Commit commit) {
    public record Commit(Author author, String message) {
    }

    public record Author(String name, String date) {
    }
}
