package edu.lexaron.simulation;

import edu.lexaron.simulation.cells.Cell;
import edu.lexaron.simulation.world.Location;
import edu.lexaron.simulation.world.World;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;

/**
 * This method handles the {@link Canvas} by displaying the current state of the {@link World} and it's {@link Cell}s.
 *
 * Author: Mirza Suljić <mirza.suljic.ba@gmail.com>
 * Date: 22.4.2018.
 */
@SuppressWarnings ("ImplicitNumericConversion")
class WorldPainter {
  private WorldPainter() {
  }

  private static final double GLOBAL_SCALE  = 2.5;

  @SuppressWarnings ({"ImplicitNumericConversion", "MagicNumber"})
  static void paintWholeWorld(World world, Canvas canvas) {
//    for (int i = 0; i < world.getHeight(); i++) {
//      for (int j = 0; j < world.getWidth(); j++)
//        Location location = Location.of(j, i);
//        if (world.findTile(location).getTrail().getAmount() > 1) {
//          canvas.getGraphicsContext2D().setFill(Color.web(world.findTile(location).getTrail().getSource().getBreed().getColorCode()));
//          canvas.getGraphicsContext2D().setGlobalAlpha(world.findTile(location).getTrail().getAmount() / 100.0);
//          canvas.getGraphicsContext2D().fillRect((j - 0.5) * GLOBAL_SCALE, (i - 0.5) * GLOBAL_SCALE, 5, 5);
//          world.findTile(location).getTrail().setAmount(world.findTile(location).getTrail().getAmount() - 1);
//        }
//        if (world.findTile(location).getSugar().getAmount() < 0) {
//          world.findTile(location).getSugar().setAmount(0);
//        }
//        canvas.getGraphicsContext2D().setGlobalAlpha(world.findTile(location).getSugar().getAmount() / 100);
//        canvas.getGraphicsContext2D().setFill(Color.web("#4d9900")); // todo Mirza : consider a GFX for sugar
//        canvas.getGraphicsContext2D().fillRect((j - 0.5) * GLOBAL_SCALE, (i - 0.5) * GLOBAL_SCALE, 5, 5);
//      }
//    }
    canvas.getGraphicsContext2D().restore();
  }
  static void paintCellVision(World world, Canvas canvas) {
    world.getAllCells().stream()
        .filter(Cell::isAlive)
        .collect(Collectors.toSet())
        .forEach(cell -> {
          int minX = cell.getX() - cell.getVision() < 0                 ? 0                    : cell.getX() - cell.getVision();
          int maxX = cell.getX() + cell.getVision() > world.getWidth()  ? world.getWidth() -1  : cell.getX() + cell.getVision();
          int minY = cell.getY() - cell.getVision() < 0                 ? 0                    : cell.getY() - cell.getVision();
          int maxY = cell.getY() + cell.getVision() > world.getHeight() ? world.getHeight() -1 : cell.getY() + cell.getVision();
          for (int i = minY; i < maxY; i++) {
            for (int j = minX; j < maxX; j++) {
              Location location = Location.of(j, i);
              if (world.findTile(location).getTrail().getAmount() > 0) {
                canvas.getGraphicsContext2D().fillRect((j - 0.5) * GLOBAL_SCALE, (i - 0.5) * GLOBAL_SCALE, 5, 5);
                canvas.getGraphicsContext2D().setFill(Color.web(world.findTile(location).getTrail().getSource().getBreed().getColorCode()));
                canvas.getGraphicsContext2D().setGlobalAlpha(world.findTile(location).getTrail().getAmount() / 100.0);
                world.findTile(location).getTrail().setAmount(world.findTile(location).getTrail().getAmount() - 1);
              }
              if (world.findTile(location).hasSugar()) {
                canvas.getGraphicsContext2D().setGlobalAlpha(world.findTile(location).getSugar().getAmount() / 100);
                canvas.getGraphicsContext2D().setFill(Color.web("#4d9900")); // todo Mirza : consider a GFX for sugar
                canvas.getGraphicsContext2D().fillRect((j - 0.5) * GLOBAL_SCALE, (i - 0.5) * GLOBAL_SCALE, 5, 5);
              }
            }
          }
        });
    canvas.getGraphicsContext2D().restore();
  }

  @SuppressWarnings ({"MagicNumber", "ImplicitNumericConversion", "NumericCastThatLosesPrecision"})
  static void paintCell(Cell cell, Canvas canvas) {
    if (cell.isAlive()) {
      canvas.getGraphicsContext2D().setGlobalAlpha(cell.getEnergy() / 100.0 + 0.5);
      canvas.getGraphicsContext2D().setFill(Color.web(cell.getBreed().getColorCode()));
      canvas.getGraphicsContext2D().setStroke(Color.web(cell.getBreed().getColorCode()));
//      paintCellFoV(cell, canvas);
      paintTargetLine(cell, canvas);
      canvas.getGraphicsContext2D().drawImage(cell.getImage(), (cell.getX() - 1.5) * GLOBAL_SCALE, (cell.getY() - 1.5) * GLOBAL_SCALE);
      canvas.getGraphicsContext2D().fillText(String.valueOf((int) cell.getEnergy()), (cell.getX() - 3) * GLOBAL_SCALE, (cell.getY() - 1.5) * GLOBAL_SCALE);
    }
    else {
      canvas.getGraphicsContext2D().setGlobalAlpha(0.2);
      canvas.getGraphicsContext2D().drawImage(cell.getImage(), cell.getX() * GLOBAL_SCALE, cell.getY() * GLOBAL_SCALE);
    }
    canvas.getGraphicsContext2D().restore();
  }

  @SuppressWarnings ({"ImplicitNumericConversion", "MagicNumber", "unused"})
  static void paintCellFoV(Cell cell, Canvas canvas) {
    canvas.getGraphicsContext2D().fillRect(
        (cell.getX() - cell.getVision() - 0.25) * GLOBAL_SCALE,
        (cell.getY() - cell.getVision() - 0.25) * GLOBAL_SCALE,
        ((cell.getVision() * 2) + 1) * GLOBAL_SCALE,
        ((cell.getVision() * 2) + 1) * GLOBAL_SCALE);
    canvas.getGraphicsContext2D().setFill(Color.web(cell.getBreed().getColorCode()));
    canvas.getGraphicsContext2D().restore();
  }

  @SuppressWarnings ({"ImplicitNumericConversion", "unused"})
  static void paintGrid(World world, Canvas canvas) {
    canvas.getGraphicsContext2D().setStroke(Color.web("#1a1a1a"));
    for (int i = 5; i < world.getWidth() * GLOBAL_SCALE; i += 5) {
      canvas.getGraphicsContext2D().strokeLine(i, 0, i, world.getHeight() * GLOBAL_SCALE);
    }
    for (int i = 5; i < world.getHeight() * GLOBAL_SCALE; i += 5) {
      canvas.getGraphicsContext2D().strokeLine(0, i, world.getWidth() * GLOBAL_SCALE, i);
    }
    canvas.getGraphicsContext2D().restore();
  }

  @SuppressWarnings ({"MagicNumber", "ImplicitNumericConversion"})
  private static void paintTargetLine(Cell cell, Canvas canvas) {
    if (cell.getFood() != null) {
      canvas.getGraphicsContext2D().setStroke(Color.web(cell.getBreed().getColorCode()));
      canvas.getGraphicsContext2D().strokeLine(
          (cell.getX() + 0.25) * GLOBAL_SCALE, (cell.getY() + 0.25) * GLOBAL_SCALE,
          (cell.getFood().getX() + 0.25) * GLOBAL_SCALE, (cell.getFood().getY() + 0.25) * GLOBAL_SCALE
      );
    }
    canvas.getGraphicsContext2D().restore();
  }
}

