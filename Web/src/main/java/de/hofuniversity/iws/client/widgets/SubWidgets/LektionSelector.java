/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hofuniversity.iws.client.widgets.SubWidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import de.hofuniversity.iws.client.PhysixLab;
import de.hofuniversity.iws.client.jsonbeans.SubjectJson;
import de.hofuniversity.iws.client.widgets.Lektion.Lektion;
import de.hofuniversity.iws.shared.dto.LessonPreview;

/**
 *
 * @author Oliver
 */
public class LektionSelector extends Composite {

    private static LektionSelectorUiBinder uiBinder = GWT.create(LektionSelectorUiBinder.class);
    private final LessonPreview lektion;
    private final SubjectJson subject;
    private final LektionPreview preview;
    @UiField
    HTMLPanel wrap;
    @UiField
    FocusPanel oImg;
    @UiField
    Image iImg;

    interface LektionSelectorUiBinder extends UiBinder<Widget, LektionSelector> {
    }

    public LektionSelector(LessonPreview lektion, SubjectJson subject, int x, int y) {
        initWidget(uiBinder.createAndBindUi(this));
        this.lektion = lektion;
        this.subject = subject;

        iImg.setUrl(lektion.getImageUrl());
        wrap.getElement().getStyle().setLeft(x, Style.Unit.PX);
        wrap.getElement().getStyle().setTop(195 + y, Style.Unit.PX);
        
        preview = new LektionPreview(lektion);
        preview.setVisible(false);
        wrap.add(preview);
        
        oImg.addMouseOverHandler(new VisibleHandler());
        oImg.addMouseOutHandler(new InvisibleHandler());
    }

    @UiHandler("oImg")
    public void openLektion(ClickEvent ev) {
        Lektion l = Lektion.build()
                .withLesson(lektion.getName())
                .withSubject(subject).create();
        PhysixLab.PAGE_CONTROLLER.changePage(l);
    }
    
    private class VisibleHandler implements MouseOverHandler {
        @Override
        public void onMouseOver(MouseOverEvent event) {
            preview.setVisible(true);
        }
    }
    
    private class InvisibleHandler implements MouseOutHandler {
        @Override
        public void onMouseOut(MouseOutEvent event) {
            preview.setVisible(false);
        }
    }
}
