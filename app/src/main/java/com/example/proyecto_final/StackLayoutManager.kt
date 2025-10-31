import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StackLayoutManager(
    context: Context?,
    private val maxVisible: Int = 4
) : LinearLayoutManager(context, VERTICAL, false) {
     var topPosition = 0

    override fun canScrollVertically(): Boolean {
        return false // desactiva el scroll vertical normal
    }
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {

        detachAndScrapAttachedViews(recycler)
        if (itemCount == 0) return
        val parentWidth = width
        val parentHeight = height

        // dibujar de arriba hacia abajo para que la carta top quede arriba
        val itemCountToDraw = (itemCount - topPosition).coerceAtMost(maxVisible)
        for (i in itemCountToDraw - 1 downTo 0) {

            val pos = topPosition + i
            val view = recycler.getViewForPosition(pos)
            addView(view)

            view.rotation = 0f
            view.scaleX = 1f
            view.scaleY = 1f
            view.translationY = 0f
            measureChildWithMargins(view, 0, 0)
            val decoratedWidth = getDecoratedMeasuredWidth(view)
            val decoratedHeight = getDecoratedMeasuredHeight(view)

            val left = (parentWidth - decoratedWidth) / 2
            val top = (parentHeight - decoratedHeight) / 2
            layoutDecorated(view, left, top, left + decoratedWidth, top + decoratedHeight)

            // apilar cartas detrás de la top
            if (i != 0) {
                val indexFromTop = i
                view.translationY = 30f * indexFromTop
                val scale = 1 - 0.05f * indexFromTop
                view.scaleX = scale
                view.scaleY = scale
            }
        }
    }



}
