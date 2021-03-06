/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hofuniversity.iws.client.widgets.SubWidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.*;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import de.hofuniversity.iws.client.PhysixLab;
import de.hofuniversity.iws.client.jsonbeans.GameJson;
import de.hofuniversity.iws.client.widgets.Game.Game;

/**
 *
 * @author Oliver
 */
public class GameSelector extends Composite {

    private static GameSelectorUiBinder uiBinder = GWT.create(GameSelectorUiBinder.class);
    @UiField
    HTMLPanel wrap;
    @UiField
    FocusPanel oImg;
    @UiField
    Image iImg;
    @UiField
    HeadingElement title;
    @UiField
    ParagraphElement text;
    private GameJson game;

    interface GameSelectorUiBinder extends UiBinder<Widget, GameSelector> {
    }

    public GameSelector(GameJson game) {
        initWidget(uiBinder.createAndBindUi(this));
        this.game = game;

        iImg.setUrl(game.getImageUrl());
        title.setInnerText(game.getTitle());
        text.setInnerText(game.getDescription());

        wrap.getElement().getStyle().setTop(180, Unit.PX);
    }

    @UiHandler("oImg")
    public void openGame(ClickEvent ev) {
        PhysixLab.PAGE_CONTROLLER.changePage(new Game(game));
    }
}
