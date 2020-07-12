package com.shu.commands;

import com.shu.ConstructionSite;
import com.shu.Facing;
import com.shu.Position;
import com.shu.blocks.Block;
import com.shu.blocks.PreservedTree;
import com.shu.blocks.VisitedBlock;
import com.shu.costs.Cost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdvanceCommand extends Command {

    private final ConstructionSite constructionSite;
    private final Position position;
    private final Integer numberOfSquares;

    public AdvanceCommand(ConstructionSite constructionSite, Position position, Integer numberOfSquares) {
        this.constructionSite = constructionSite;
        this.position = position;
        this.numberOfSquares = numberOfSquares;
    }

    @Override
    protected CommandResult executeCommand() {
        return move(position, getNextPositionFunc(position.getFacing()), numberOfSquares);
    }

    private CommandResult move(Position currentPosition, Function<Position, Position> nextPositionFunc, Integer steps) {
        Position nextPosition = nextPositionFunc.apply(currentPosition);

        if (!constructionSite.isOnSite(nextPosition)) {
            return new CommandResult(currentPosition, Collections.emptyList(), CommandState.BEYOND_BOUNDARIES);
        }

        Block block = getBlock(nextPosition);
        List<Cost> costs = block.getCosts();
        CommandState state = block.getClass() == PreservedTree.class ? CommandState.REMOVE_PRESERVED_TREE : CommandState.SUCCESS;

        constructionSite.getBlocks()[nextPosition.getyDimension()][nextPosition.getxDimension()] = new VisitedBlock();

        if (state == CommandState.REMOVE_PRESERVED_TREE || steps == 1) {
            return new CommandResult(nextPosition, costs, state);
        }

        CommandResult nextMoveResult = move(nextPosition, nextPositionFunc, steps - 1);
        return new CommandResult(nextPosition,
                Stream.of(costs, nextMoveResult.getCosts()).flatMap(Collection::stream).collect(Collectors.toList()),
                nextMoveResult.getCommandState());
    }

    private Block getBlock(Position position) {
        return constructionSite.getBlocks()[position.getyDimension()][position.getxDimension()];
    }

    private Function<Position, Position> getNextPositionFunc(Facing facing) {
        switch (facing) {
            case EAST: {
                return (Position p) -> new Position(p.getxDimension() + 1, p.getyDimension(), p.getFacing());
            }
            case NORTH: {
                return (Position p) -> new Position(p.getxDimension(), p.getyDimension() - 1, p.getFacing());
            }
            case WEST: {
                return (Position p) -> new Position(p.getxDimension() - 1, p.getyDimension(), p.getFacing());
            }
            default: {
                return (Position p) -> new Position(p.getxDimension(), p.getyDimension() + 1, p.getFacing());
            }
        }
    }
}
