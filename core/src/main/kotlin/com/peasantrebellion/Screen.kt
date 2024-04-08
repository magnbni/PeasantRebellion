package com.peasantrebellion

import com.badlogic.gdx.graphics.OrthographicCamera
import com.peasantrebellion.controller.Controller
import com.peasantrebellion.controller.GameController
import com.peasantrebellion.controller.GameEndController
import com.peasantrebellion.controller.MainMenuController
import com.peasantrebellion.model.Game
import com.peasantrebellion.view.GameEndView
import com.peasantrebellion.view.GameView
import com.peasantrebellion.view.MainMenuView
import com.peasantrebellion.view.View

class Screen private constructor(
    private var controller: Controller,
    private var view: View,
) {
    fun update(deltaTime: Float) {
        controller.update(deltaTime)
        view.render()
    }

    fun dispose() {
        view.dispose()
    }

    fun resize(
        width: Int,
        height: Int,
    ) {
        view.resize(width, height)
    }

    companion object {
        fun game(): Screen {
            val game = Game()

            // Camera is used both for rendering and user input.
            val camera = OrthographicCamera(Game.WIDTH, Game.HEIGHT)
            camera.setToOrtho(false, Game.WIDTH, Game.HEIGHT)
            camera.update()

            return Screen(
                GameController(game, camera),
                GameView(game, camera),
            )
        }

        fun gameEnd(conclusion: Game.Conclusion): Screen {
            return Screen(
                GameEndController(),
                GameEndView(),
            )
        }

        fun mainMenu(): Screen {
            // Camera is used both for rendering and user input.
            val camera = OrthographicCamera(MainMenuView.WIDTH, MainMenuView.HEIGHT)
            camera.setToOrtho(false, MainMenuView.WIDTH, MainMenuView.HEIGHT)
            camera.update()

            val mainMenuView = MainMenuView(camera)
            return Screen(MainMenuController(mainMenuView), mainMenuView)
        }
    }
}
