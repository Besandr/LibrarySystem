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

    private long authorId;
    private long keywordId;
    private String bookTitle;

    @Override
    public void fill(HttpServletRequest request) {
        authorId = getLongPropertyFromRequest(request, "authorId");
        keywordId = getLongPropertyFromRequest(request, "keywordId");
        bookTitle = getPropertyFromRequest(request, "bookTitle");
    }

    @Override
    public ActionErrors validate() {
        return new ActionErrors();
    }


}
