package com.library.web.controllers.forms;

import com.library.web.controllers.ActionErrors;
import lombok.*;

import javax.servlet.http.HttpServletRequest;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderBookForm extends ActionForm {

    private long bookId;

    /**
     * {@inheritDoc}
     */
    @Override
    public void fill(HttpServletRequest request) {
        bookId = getLongPropertyFromRequest(request, "bookId");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionErrors validate() {
        return new ActionErrors();
    }


}
