package com.gubertmc.plugin.main.snakegame;

import com.gubertmc.MazeGeneratorPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class Arena {

    private final MazeGeneratorPlugin plugin;
    private final Block arenaLocationBlock;
    private final int size;

    private final Location[][] arenaBlockLocations;
    private long time;

    public Arena(MazeGeneratorPlugin plugin, Block arenaLocationBlock, int size) {
        this.plugin = plugin;
        this.arenaLocationBlock = arenaLocationBlock;
        this.size = size;

        this.arenaBlockLocations = new Location[size][size];
        this.time = 0L;
    }

    public void startGame() {
        spawnArena(Material.BLACK_WOOL);

        // spawn new food object
        Food food = spawnFood(Material.YELLOW_WOOL);
        food.spawn();

        // run the algorithm
        Snake snake = new Snake(plugin, arenaBlockLocations);
        snake.pursueFood(food);

        boolean gameOver = false;
        while (!gameOver) {
            // if the snake ate the food
            if (snake.getFoodStatus()) {
                // spawn new food
                food = spawnFood(Material.YELLOW_WOOL);
                food.spawn();
                snake.pursueFood(food);
            }
            if (!snake.getSnakeIsAlive()) {
                gameOver = true;
            }
        }

        // if the algorithm runs into the food change the food status to false (aka eaten)


    }

    public void spawnArena(Material arenaMaterial) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Location location = new Location(
                        arenaLocationBlock.getWorld(),
                        arenaLocationBlock.getX() + i,
                        arenaLocationBlock.getY(),
                        arenaLocationBlock.getZ() + j
                );
                location.getBlock().setType(arenaMaterial);
                arenaBlockLocations[i][j] = location;
            }
            time += 1L;
        }
    }

    public Food spawnFood(Material foodMaterial) {
        int xCoordinate = (int) (Math.random() * size);
        int yCoordinate = (int) (Math.random() * size);

        return new Food(arenaLocationBlock, xCoordinate, yCoordinate, false);
    }
}
