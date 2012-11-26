/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hofuniversity.iws.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import de.hofuniversity.iws.shared.dto.SessionDTO;
import de.hofuniversity.iws.shared.services.LoginServiceAsync;

/**
 *
 * @author Daniel Heinrich <dannynullzwo@gmail.com>
 */
public class SessionPage extends Composite {

    private SessionPagetUiBinder uiBinder = GWT.create(SessionPagetUiBinder.class);

    interface SessionPagetUiBinder extends UiBinder<Widget, SessionPage> {
    }
    private final LoginServiceAsync loginService;
    private final SessionDTO session;
    @UiField
    private SpanElement sessionLabel;

    public SessionPage(LoginServiceAsync loginService, SessionDTO session) {
        this.loginService = loginService;
        this.session = session;
        initWidget(uiBinder.createAndBindUi(this));
        sessionLabel.setInnerText("id: " + session.getSessionID() + " expires: " + session.getExpireDate().toString());
    }

    @UiHandler("logout")
    public void logout() {
        loginService.logout(session.getSessionID(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                RootPanel.get().clear();
                RootPanel.get().add(new LoginPage(loginService));
            }

            @Override
            public void onSuccess(Void result) {
                RootPanel.get().clear();
                RootPanel.get().add(new LoginPage(loginService));
            }
        });
    }
}