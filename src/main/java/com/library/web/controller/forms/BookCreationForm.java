package com.library.web.controller.forms;

import com.library.web.controller.ActionErrors;
import lombok.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Class represents book creation html-form
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCreationForm extends ActionForm {

    private String title;
    private String yearString;
    private int year;
    private String description;
    private List<String> newAuthorFirstNames;
    private List<String> newAuthorLastNames;
    private List<String> newKeywords;
    private List<Long> oldAuthorsId;
    private List<Long> oldKeywordsId;

    /**
     * {@inheritDoc}
     */
    @Override
    public void fill(HttpServletRequest request) {
        title = getPropertyFromRequest(request, "title");
        yearString = getPropertyFromRequest(request, "year");
        description = getPropertyFromRequest(request, "description");

        newAuthorFirstNames = getPropertyListFromRequest(request, "newAuthorFirstName");
        newAuthorLastNames = getPropertyListFromRequest(request, "newAuthorLastName");
        removeEmptyNewAuthors(newAuthorFirstNames, newAuthorLastNames);

        newKeywords = getPropertyListFromRequest(request, "newKeyword")
                .stream()
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        oldAuthorsId = getPropertyListFromRequest(request, "oldAuthorId")
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
        oldKeywordsId = getPropertyListFromRequest(request, "oldKeywordsId")
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionErrors validate() {
        ActionErrors errors = new ActionErrors();

        titleValidation(errors, title);
        yearValidation(errors, yearString);
        descriptionValidation(errors, description);
        authorsValidation(errors, newAuthorFirstNames, newAuthorLastNames, oldAuthorsId);

        return errors;
    }

    /**
     * Removes from given lists records where first and last
     * author names are empty
     * @param newAuthorFirstNames - list with first names of new authors
     * @param newAuthorLastNames - list with last names of new authors
     */
    private void removeEmptyNewAuthors(List<String> newAuthorFirstNames, List<String> newAuthorLastNames) {
        for (int i = 0; i < this.newAuthorFirstNames.size(); i++) {
            if (this.newAuthorFirstNames.get(i).isEmpty()
                    && this.newAuthorLastNames.get(i).isEmpty()) {
                newAuthorLastNames.remove(i);
                newAuthorFirstNames.remove(i);
            }
        }
    }

    /**
     * Validates book's title
     */
    private void titleValidation(ActionErrors errors, String title) {
        if (title.isEmpty()) {
            errors.addError("emptyTitle", "bookManagement.bookForm.errors.title");
        }
    }

    /**
     * Validates book's issue year
     */
    private void yearValidation(ActionErrors errors, String yearString) {
        if (yearString.isEmpty()) {
            errors.addError("year", "bookManagement.bookForm.errors.emptyYear");
            return;
        }

        try {
            year = Integer.parseInt(yearString);
        } catch (NumberFormatException e) {
            //Entered invalid year
            errors.addError("year", "bookManagement.bookForm.errors.invalidYear");
            return;
        }

        if (year < 1800 || year > LocalDate.now().getYear()) {
            errors.addError("year", "bookManagement.bookForm.errors.invalidYear");
        }
    }

    /**
     * Validates book's description
     */
    private void descriptionValidation(ActionErrors errors, String description) {
        if (description.isEmpty()) {
            errors.addError("emptyDescription", "bookManagement.bookForm.errors.emptyDescription");
        }
    }

    /**
     * Validates book's authors
     */
    private void authorsValidation(ActionErrors errors, List<String> newAuthorFirstNames, List<String> newAuthorLastNames, List<Long> oldAuthorsId) {

        for (int i = 0; i < newAuthorFirstNames.size(); i++) {
            if (newAuthorLastNames.get(i).isEmpty()
                    && !newAuthorFirstNames.get(i).isEmpty()) {
                errors.addError("newAuthors", "bookManagement.bookForm.errors.emptyLastName");
                return;
            }
        }

        if (newAuthorLastNames.isEmpty() && oldAuthorsId.isEmpty()) {
            errors.addError("newAuthors", "bookManagement.bookForm.errors.emptyAuthors");
            return;
        }

        String namePattern = "(\\p{Alpha}+(-?\\p{Alpha})?\\.?\\s?)+";
        Pattern pattern = Pattern.compile(namePattern, Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher;
        for (String firstName : newAuthorFirstNames) {
            matcher = pattern.matcher(firstName);
            if (!firstName.isEmpty() && !matcher.matches()) {
                errors.addError("newAuthors", "bookManagement.bookForm.errors.namePatternConformance");
                return;
            }
        }

        for (String lastName : newAuthorLastNames) {
            matcher = pattern.matcher(lastName);
            if (!lastName.isEmpty() && !matcher.matches()) {
                errors.addError("newAuthors", "bookManagement.bookForm.errors.namePatternConformance");
            }
        }
    }
}
