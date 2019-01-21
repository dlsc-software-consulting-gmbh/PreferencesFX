package com.dlsc.preferencesfx.history;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.dlsc.preferencesfx.model.Setting;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link Change}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class ChangeTest {

  String s1 = "String 1";
  String s2 = "String 2";
  String s3 = "String 3";
  String s4 = "String 4";
  private ObservableList<String> list1;
  private ObservableList<String> list2;
  Change c;
  Setting mockSetting = mock(Setting.class);

  @Before
  public void setUp() throws Exception {
    list1 = FXCollections.observableArrayList();
    list2 = FXCollections.observableArrayList();
  }

  @Test
  public void undo() {
  }

  @Test
  public void redo() {
  }

  @Test
  public void isRedundantTwoEmptyLists() {
    c = new Change(mockSetting, list1, list2);
    assertTrue(c.isRedundant());

    // changes to the original list don't change the result
    list1.add(s1);
    assertTrue(c.isRedundant());
  }

  @Test
  public void isRedundantOneEmptyList() {
    // first list empty
    list1.add(s1);
    c = new Change(mockSetting, list1, list2);
    assertFalse(c.isRedundant());

    // second list empty
    list1.remove(s1);
    list2.add(s1);
    c = new Change(mockSetting, list1, list2);
    assertFalse(c.isRedundant());
  }

  @Test
  public void isRedundantSameObjects() {
    list1.addAll(s1, s2, s3);
    list2.addAll(s1, s2, s3);
    c = new Change(mockSetting, list1, list2);
    assertTrue(c.isRedundant());
  }

  @Test
  public void isRedundantSameObjectsDifferentOrder() {
    list1.addAll(s1, s2, s3);
    list2.addAll(s1, s3, s2);
    c = new Change(mockSetting, list1, list2);
    assertFalse(c.isRedundant());
  }

  @Test
  public void isRedundantDifferentObjects() {
    list1.addAll(s1, s2);
    list2.addAll(s3, s4);
    c = new Change(mockSetting, list1, list2);
    assertFalse(c.isRedundant());
  }

  @Test
  public void isRedundantSameObjectsDifferentCardinalities() {
    list1.addAll(s1, s2, s2);
    list2.addAll(s1, s1, s2);
    c = new Change(mockSetting, list1, list2);
    assertFalse(c.isRedundant());
  }

  @Test
  public void isListChange() {
  }

  @Test
  public void getTimestamp() {
  }
}
