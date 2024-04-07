package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.components.AnimationComponent
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.TextureComponent
import com.peasantrebellion.model.components.UserControlledComponent

const val ENEMY_MOVEMENT_SPEED = 50f // Temporary value, can be adjusted

class EnemyMovementSystem : IteratingSystem(
    Family.all(
        BodyComponent::class.java,
        TextureComponent::class.java,
        AnimationComponent::class.java,
    ).exclude(UserControlledComponent::class.java).get(),
) {
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val enemyFamily = Family.all(BodyComponent::class.java).exclude(UserControlledComponent::class.java).get()
    private var direction = 1 // The direction of the peasant, 1 for right, -1 for left, 0 for no movement

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        val enemies = engine.getEntitiesFor(enemyFamily)
        var moveDown = false
        // Check if any of the peasants hit a wall. If so, move down and change direction
        for (enemy in enemies) {
            val body = bodyMapper[enemy].body
            if (((body.x + body.width) > Game.WIDTH && direction > 0) || (body.x < 0f && direction < 0)) {
                // A peasant hit either the right or left side, all enemies should move down and change direction
                moveDown = true
                direction = -direction
            }
        }

        if (moveDown) {
            enemies.forEach { e ->
                val b = bodyMapper[e].body
                b.y -= b.height / 2
            }
        }
    }

    override fun processEntity(
        entity: Entity?,
        deltaTime: Float,
    ) {
        val body = bodyMapper[entity].body
        // Move the peasant to either the right or left, depending on the direction
        body.x += ENEMY_MOVEMENT_SPEED * deltaTime * direction
    }
}
