package com.dlsc.preferencesfx.history;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 * Created by François Martin on 11.12.2017.
 */
public class HistoryButtonBox extends HBox {
  private History history;

  GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");

  Button undoBtn = new Button("", fontAwesome.create(FontAwesome.Glyph.UNDO));
  Button redoBtn = new Button("", fontAwesome.create(FontAwesome.Glyph.REPEAT));

  public HistoryButtonBox(History history) {
    this.history = history;
    getChildren().addAll(undoBtn, redoBtn);
    undoBtn.setOnAction(event -> history.undo());
    undoBtn.disableProperty().bind(history.undoAvailableProperty().not());
    redoBtn.setOnAction(event -> history.redo());
    redoBtn.disableProperty().bind(history.redoAvailableProperty().not());

  }

}
