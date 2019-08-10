package com.library.web.controllers.forms;

import com.library.web.controllers.ActionErrors;
import lombok.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Class represents book searching html-form
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookSearchForm extends ActionForm {

    private String authorId;
    private String keywordId;
    private String bookTitle;

    @Override
    public void fill(HttpServletRequest request) {
        authorId = getPropertyFromRequest(request, "authorId");
        keywordId = getPropertyFromRequest(request, "keywordId");
        bookTitle = getPropertyFromRequest(request, "bookTitle");
    }

    @Override
    public ActionErrors validate() {
        return new ActionErrors();
    }


}
