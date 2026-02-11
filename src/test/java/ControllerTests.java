import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import mocks.ModelRecordListenerMock;
import mocks.ModelRecordMovesMock;
import mocks.StrategyRecordMoveMock;
import org.junit.Test;
import sanguine.controller.AiPlayer;
import sanguine.controller.PlayablePlayer;
import sanguine.controller.PublisherSanguineModel;
import sanguine.controller.SanguineController;
import sanguine.model.Player;
import sanguine.strategies.FillFirst;
import sanguine.view.SanguineVisualView;

/**
 * Tests for SanguineController.
 */
public class ControllerTests {

  @Test
  public void testControllerIsAddedAsModelListener() {
    Appendable log = new StringBuilder();
    PublisherSanguineModel model = new ModelRecordListenerMock(log);
    SanguineVisualView view = new SanguineVisualView(model, Player.RED);
    PlayablePlayer player = new AiPlayer(new FillFirst());
    SanguineController controller = new SanguineController(model, view, player, Player.RED);
    assertEquals("added listener", log.toString());
  }

  @Test
  public void testControllerMakesMoves() {
    Appendable log = new StringWriter();
    PublisherSanguineModel model = new ModelRecordMovesMock(log);
    SanguineVisualView view = new SanguineVisualView(model, Player.RED);
    PlayablePlayer player = new AiPlayer(new StrategyRecordMoveMock(log));
    SanguineController controller = new SanguineController(model, view, player, Player.RED);
    controller.turnChanged(Player.RED);
    controller.passTurn();
    System.out.println(log);
    assertEquals("pass\nmove chosen", log.toString());
  }
}
