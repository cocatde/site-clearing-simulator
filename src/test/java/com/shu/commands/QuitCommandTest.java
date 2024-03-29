package com.shu.commands;

import com.shu.Facing;
import com.shu.Position;
import com.shu.costs.Cost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class QuitCommandTest {
    @Test
    @DisplayName("When execute an Quit command, it should return a Invalid state")
    void executeQuitState() {
        Position originalPosition = new Position(0, 0, Facing.NORTH);
        QuitCommand quitCommand = new QuitCommand(originalPosition);
        CommandResult result = quitCommand.execute();

        CommandState actualState = result.getCommandState();
        CommandState expectedState = CommandState.QUIT;

        assertThat(actualState).isEqualTo(expectedState);
    }

    @Test
    @DisplayName("When execute an Quit command, it should return same position as before")
    void executeSamePosition() {
        Position originalPosition = new Position(0, 0, Facing.NORTH);
        QuitCommand quitCommand = new QuitCommand(originalPosition);
        CommandResult result = quitCommand.execute();

        Position actualPosition = result.getPosition();

        assertThat(actualPosition).isEqualTo(originalPosition);
    }

    @Test
    @DisplayName("When execute an Quit command, it shouldn't have any cost")
    void executeNoCost() {
        Position originalPosition = new Position(0, 0, Facing.NORTH);
        QuitCommand quitCommand = new QuitCommand(originalPosition);
        CommandResult result = quitCommand.execute();

        List<Cost> actualCost = result.getCosts();

        assertThat(actualCost.size()).isEqualTo(0);
    }

}