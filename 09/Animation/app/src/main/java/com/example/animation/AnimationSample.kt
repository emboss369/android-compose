package com.example.animation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.animation.ui.theme.AnimationTheme
import com.example.animation.ui.theme.Green300
import com.example.animation.ui.theme.Green800
import com.example.animation.ui.theme.Purple700
import kotlinx.coroutines.launch

class AnimationSample : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      AnimationTheme {
        // A surface container using the 'background' color from the theme
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colors.background
        ) {
          Greeting2("Android")
        }
      }
    }
  }
}

@Composable
fun Greeting2(name: String) {
  Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
  AnimationTheme {
    Greeting2("Android")
  }
}

// 高レベルアニメーションと低レベルアニメーション
// 低レベルなアニメーションとは、低レベルなAPIを使ったアニメーションです。
// 低レベルなAPIは、コーディング量が増えて大変ですが、その分細かな制御を行うことができます。
// 高レベルなアニメーションとは、高レベルなAPIを使ったアニメーションです。
// 高レベルなAPIは、コーディング量がすくなく手軽にアニメーションを実装できます。しかし細かな制御はできません。

// https://developer.android.com/jetpack/compose/animation?hl=ja#high-level-apis
// 高レベル アニメーション API

// AnimatedVisibility
// 表示と非表示をアニメーション化します。

@Composable
fun AnimatedVisibilityView() {
  var show by remember { mutableStateOf(true) }
  Box {
    Column {
      AnimatedVisibility(visible = show) {
        Text(
          modifier = Modifier
            .size(size = 100.dp)
            .background(color = Color.Magenta),
          text = "アニメーションします"
        )

      }
      Button(onClick = { show = !show }) {
        Text(if (show) "表示" else "非表示")
      }

    }
  }
}

@Preview(widthDp = 100, heightDp = 150, showBackground = true)
@Composable
fun AnimatedVisibilityViewPreview() {
  AnimationTheme {
    AnimatedVisibilityView()
  }
}

@Composable
fun TransitionView(
  show: Boolean,
  title: String,
  enter: EnterTransition,
  exit: ExitTransition
) {


  Column() {
    Text(text = title)
    Row(
      modifier = Modifier
        .size(width = 160.dp, height = 70.dp)
        .clipToBounds()
    ) {
      AnimatedVisibility(
        visible = show,
        enter = enter,
        exit = exit
      ) {
        Image(
          painter = painterResource(id = R.drawable.slide04),
          contentDescription = ""
        )
      }
    }

  }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TransitionGrid() {
  val anim = listOf(
    "fadeIn" to fadeIn(), "slideIn" to slideIn { fullSize ->
      IntOffset(fullSize.width, fullSize.height)
    },
    "slideInHorizontally" to slideInHorizontally(),
    "slideInVertically" to slideInVertically(),
    "scaleIn" to scaleIn(), "expandIn" to expandIn(),
    "expandHorizontally" to expandHorizontally(),
    "expandVertically" to expandVertically(),
    "fadeOut" to fadeOut(), "slideOut" to slideOut { fullSize ->
      IntOffset(fullSize.width, fullSize.height)
    },
    "slideOutHorizontally" to slideOutHorizontally(),
    "slideOutVertically" to slideOutVertically(),
    "scaleOut" to scaleOut(), "shrinkOut" to shrinkOut(),
    "shrinkHorizontally" to shrinkHorizontally(),
    "shrinkVertically" to shrinkVertically()
  )
    var show by remember { mutableStateOf(false) }
  Column() {


    Row() {
      Column() {
        TransitionView(
          show = show,
          title = "fadeIn",
          enter = fadeIn(),
          exit = ExitTransition.None
        )
        TransitionView(
          show = show,
          title = "slideIn",
          enter = slideIn() { fullSize ->
            IntOffset(fullSize.width, fullSize.height)
          },
          exit = ExitTransition.None
        )
        TransitionView(
          show = show,
          title = "slideInHorizontally",
          enter = slideInHorizontally(),
          exit = ExitTransition.None
        )
        TransitionView(
          show = show,
          title = "slideInVertically",
          enter = slideInVertically(),
          exit = ExitTransition.None
        )
        TransitionView(
          show = show,
          title = "scaleIn",
          enter = scaleIn(),
          exit = ExitTransition.None
        )
        TransitionView(
          show = show,
          title = "expandIn",
          enter = expandIn(),
          exit = ExitTransition.None
        )
        TransitionView(
          show = show,
          title = "expandHorizontally",
          enter = expandHorizontally(),
          exit = ExitTransition.None
        )
        TransitionView(
          show = show,
          title = "expandVertically",
          enter = expandVertically(),
          exit = ExitTransition.None
        )
      }
      Column() {
        TransitionView(
          show = !show,
          title = "fadeOut",
          enter = EnterTransition.None,
          exit = fadeOut()
        )
        TransitionView(
          show = !show,
          title = "slideOut",
          enter = EnterTransition.None,
          exit = slideOut() { fullSize ->
            IntOffset(fullSize.width, fullSize.height)
          }
        )
        TransitionView(
          show = !show,
          title = "slideOutHorizontally",
          enter = EnterTransition.None,
          exit = slideOutHorizontally()
        )
        TransitionView(
          show = !show,
          title = "slideOutVertically",
          enter = EnterTransition.None,
          exit = slideOutVertically()
        )
        TransitionView(
          show = !show,
          title = "slideOutVertically",
          enter = EnterTransition.None,
          exit = scaleOut()
        )
        TransitionView(
          show = !show,
          title = "shrinkOut",
          enter = EnterTransition.None,
          exit = shrinkOut()
        )
        TransitionView(
          show = !show,
          title = "shrinkHorizontally",
          enter = EnterTransition.None,
          exit = shrinkHorizontally()
        )
        TransitionView(
          show = !show,
          title = "shrinkVertically",
          enter = EnterTransition.None,
          exit = shrinkVertically()
        )
      }
    }
    Button(onClick = { show = !show }) {
      Text("ANIMATION")
    }
  }

}

@Preview(showBackground = true)
@Composable
fun TransitionSampleViewPreview() {
  AnimationTheme {
    TransitionGrid()
  }
}

@Composable
fun AnimatedVisibilityExample() {
  // AnimatedVisibilityは、複数のアニメーションを適用することもできます。 ＋ で繋ぐと複数アニメーションが
  // 一度に適用されます。slideInHorizontallyとfadeInを一度に適用してみましょう。
  var show by remember { mutableStateOf(false) }

  Column() {
    Row(
      modifier = Modifier
        .size(width = 100.dp, height = 100.dp)
        .clipToBounds()
    ) {
      AnimatedVisibility(
        visible = show,
        enter = slideInHorizontally() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut()
      ) {
        Image(
          painter = painterResource(id = R.drawable.slide04),
          contentDescription = ""
        )
      }
    }
    Button(onClick = { show = !show }) {
      Text("ANIMATION")
    }
  }
}

@Preview(showBackground = true)
@Composable
fun AnimatedVisibilityExamplePreview() {
  AnimationTheme {
    AnimatedVisibilityExample()
  }
}

@Composable
fun AnimatedVisibilityAutomatically() {
  // AnimatedVisibilityは、vislbleStateというパラメータを受け取る派生関数もあります。
  // これを使うと、AnimatedVisibilityが表示されると同時にアニメーションを開始することができます。

  // visibleStatelには、MutableTransitionStateのインスタンスを指定します。これは
  // AnimatedVisibilityはアニメーションの状態を取得することもできます。

  var state = remember {
    MutableTransitionState(false).apply {
      // すぐにアニメーションを開始する。
      targetState = true
    }
  }
  Column() {
    Row(
      modifier = Modifier
        .size(width = 100.dp, height = 100.dp)
        .clipToBounds()
    ) {
      AnimatedVisibility(
        visibleState = state,
        enter = slideInHorizontally(
          animationSpec = tween(2000)
        ) + fadeIn(
          animationSpec = tween(2000)
        ),
        exit = shrinkVertically(
          animationSpec = tween(2000)
        ) + fadeOut(
          animationSpec = tween(2000)
        )
      ) {
        Image(
          painter = painterResource(id = R.drawable.slide04),
          contentDescription = ""
        )
      }

    }
    // Use the MutableTransitionState to know the current animation state
    // of the AnimatedVisibility.
    Text(
      text = when {
        state.isIdle && state.currentState -> "Visible"
        !state.isIdle && state.currentState -> "Disappearing"
        state.isIdle && !state.currentState -> "Invisible"
        else -> "Appearing"
      }
    )
    Button(onClick = { state.targetState = !state.targetState }) {
      Text("ANIMATION")
    }
  }
}

@Preview(showBackground = true)
@Composable
fun AnimatedVisibilityAutomaticallyPreview() {
  AnimationTheme {
    AnimatedVisibilityAutomatically()
  }
}


// https://developer.android.com/jetpack/compose/animation?hl=ja#animate-as-state
// animate*AsState 関数は、Compose で単一の値をアニメーション化するための最もシンプルなアニメーション API です。
// 終了値（またはターゲット値）を指定するだけで、API は現在の値から指定された値までのアニメーションを開始します。

// Color.ktに２行追加
//val Green300 = Color(0xFF81C784)
//val Green800 = Color(0xFF2E7D32)

@Composable
fun ChangeTwoWindow() {
  val state = remember { mutableStateOf(false) }

  // https://developer.android.com/jetpack/compose/animation?hl=ja#animate-as-state
  // 単純な値の変化をアニメーション化するには、
  // animate*AsState API を使用します。
  // 変化する値を、対応する animate*AsState コンポーザブルのバリアント
  // （この場合は animateColorAsState）でラップすることで、
  // アニメーション値を作成できます。
  // 戻り値は State<T> オブジェクトであるため、
  // ローカルの委譲プロパティと by 宣言を使用して、
  // 通常の変数のように扱うことができます。

  //val backgroundColor = if (state.value) Green300 else Green800
  val backgroundColor by animateColorAsState(if (state.value) Green300 else Green800)

  // by デリゲート構文には、次のインポートが必要です。
  // import androidx.compose.runtime.getValue
  // import androidx.compose.runtime.setValue


  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("TopAppBar") },
        backgroundColor = Purple700
      )
    },
    backgroundColor = backgroundColor,
    content = { innerPadding ->

      Button(
        modifier = Modifier.padding(innerPadding),
        onClick = { state.value = !state.value }
      ) {
        Text("背景切り替え")
      }

    }
  )
}

@Preview(showBackground = true)
@Composable
fun ChangeTwoWindowPreview() {
  AnimationTheme {
    ChangeTwoWindow()
  }
}

@Composable
fun ChangeBoxSize() {

  var size by remember {
    mutableStateOf(0.5f)
  }

  val state by remember { mutableStateOf(false) }

  //val backgroundColor by animateColorAsState(if (state) Green300 else Green800)


  Box(
    modifier = Modifier
      .size(size = 300.dp),
    contentAlignment = Alignment.Center
  ) {
    Box(
      modifier = Modifier
        .scale(scale = size)
        .size(size = 100.dp)
        .background(color = Color.Magenta)
        .clickable {
          size = if (size == 2f) 0.5f else 2f
        }
    ) {
    }
  }


}

@Preview(showBackground = true)
@Composable
fun ChangeBoxSizePreview() {
  AnimationTheme {
    ChangeBoxSize()
  }
}

@Composable
fun AnimationBoxSize(animationSpec: AnimationSpec<Float>) {

  var size by remember {
    mutableStateOf(0.5f)
  }

//    val animationScale by animateFloatAsState(
//        targetValue = size,
  // バネのアニメーション
//        animationSpec = spring(
//            dampingRatio = Spring.DampingRatioHighBouncy,
//            stiffness = Spring.StiffnessMedium
//        )
//    )
  // アニメーションを差し替えれるように改良
  val animationScale by animateFloatAsState(
    targetValue = size,
    animationSpec = animationSpec
  )


  val state by remember { mutableStateOf(false) }
  val backgroundColor by remember { mutableStateOf(if (state) Green300 else Green800) }
  //val backgroundColor by animateColorAsState(if (state) Green300 else Green800)


  Box(
    modifier = Modifier
      .size(size = 300.dp),
    contentAlignment = Alignment.Center
  ) {
    Box(
      modifier = Modifier
        .scale(scale = animationScale)
        .size(size = 100.dp)
        .background(color = Color.Magenta)
        .clickable {
          size = if (size == 2f) 0.5f else 2f
        }
    ) {
    }
  }
}

@Preview(showBackground = true)
@Composable
fun SpringAnimationBoxSizePreview() {
  AnimationTheme {
    AnimationBoxSize(
      spring(
        // dampingRatio は、ばねの弾性を定義します。デフォルト値は Spring.DampingRatioNoBouncy です。
        dampingRatio = Spring.DampingRatioHighBouncy,
        // stiffness は、終了値までのばねの移動速度を定義します。デフォルト値は Spring.StiffnessMedium です。
        stiffness = Spring.StiffnessMedium
      )
    )
  }
}

// https://developer.android.com/jetpack/compose/animation?hl=ja#tween
@Preview(showBackground = true)
@Composable
fun TweenAnimationBoxSizePreview() {
  AnimationTheme {
    AnimationBoxSize(
      // トゥイーンアニメーション
      // イージング カーブを使用して、指定された時間で目標値に向かってアニメーション化します。
      tween(
        // アニメーションの継続時間
        durationMillis = 2000,
        // アニメーションの開始を延期する。
        delayMillis = 40,
        // 開始と終了の間を補間するために使用されるイージングカーブ
        easing = LinearOutSlowInEasing
      )
    )
  }
}


@Preview(showBackground = true)
@Composable
fun RepeatableAnimationBoxSizePreview() {
  AnimationTheme {
    AnimationBoxSize(
      // 繰り返しアニメーション
      // 初期値とターゲットの間で繰り返すアニメーションを作成します。
      repeatable(
        // iterations - 反復の合計数．繰り返す場合は，1より大きくなければなりません．
        iterations = 3,
        //  animation - 繰り返し再生されるアニメーションです。
        animation = tween(durationMillis = 500),
        // repeatMode - アニメーションを最初から繰り返すか（ RepeatMode.Restart など）、最後から繰り返すか（ RepeatMode.Reverse など）です。
        repeatMode = RepeatMode.Reverse
        // initialStartOffset - アニメーションの開始をオフセットします。
      )
    )
  }
}


//infiniteRepeatable
//infiniteRepeatable は repeatable に似ていますが、繰り返し回数が無限である点が異なります。
@Preview(showBackground = true)
@Composable
fun InfiniteRepeatableAnimationBoxSizePreview() {
  AnimationTheme {
    AnimationBoxSize(
      // 無限繰り返しアニメーション
      // 繰り返しアニメーションと同じですが無限に繰り返します。
      infiniteRepeatable(
        // animation - 繰り返し再生されるアニメーションです。
        // repeatMode - アニメーションを最初から繰り返すか（ RepeatMode.Restart など）、最後から繰り返すか（ RepeatMode.Reverse など）です。
        // initialStartOffset - アニメーションの開始をオフセットします。
        animation = tween(durationMillis = 500),
        repeatMode = RepeatMode.Reverse
      )
    )
  }
}

// https://developer.android.com/jetpack/compose/animation?hl=ja#keyframesß
// keyframes
//keyframes は、アニメーションの持続時間内の異なるタイムスタンプで指定されたスナップショット値に基づいてアニメーション化します。アニメーション値は、常に 2 つのキーフレーム値の間で補間されます。これらのキーフレームごとに、イージングを指定して補間曲線を設定できます。
@Preview(showBackground = true)
@Composable
fun KeyframesAnimationBoxSizePreview() {
  AnimationTheme {
    AnimationBoxSize(
      keyframes {
        durationMillis = 1000
        0.0f at 0 with LinearOutSlowInEasing // for 0-750 ms
        1.5f at 750 with FastOutLinearInEasing // for 750-100 ms
        2.0f at 1000
      }
    )
  }
}

//snap
//snap は、値をすぐに終了値に切り替える特別な AnimationSpec です。delayMillis を指定して、アニメーションの開始を遅らせることができます。
@Preview(showBackground = true)
@Composable
fun SnapAnimationBoxSizePreview() {
  AnimationTheme {
    AnimationBoxSize(
      //アニメーションする値をすぐに終了値に切り替えるための
      // Snapアニメーションを作成する。
      //パラメータは以下のとおりです。
      //delayMillis - アニメーションが実行されるまでの
      // 待ち時間（ミリ秒）です。デフォルトでは0です。
      snap(
        delayMillis = 1000
      )
    )
  }
}

// Compose には、Float、Color、Dp、Size、Offset、Rect、Int、IntOffset、IntSize 用の animate*AsState 関数が、すぐに使える状態で用意されています。
//animateFloatAsState
//animateColorAsState
//animateDpAsState
//animateSizeAsState
//animateOffsetAsState
//animateRectAsState
//animateIntAsState
//animateIntOffsetAsState
//animateIntSizeAsState


// https://semicolonspace.com/android-jetpack-compose-animateasstate/


// https://developer.android.com/jetpack/compose/animation?hl=ja#animateContentSize
// animateContentSize
// animateContentSize 修飾子は、サイズ変更をアニメーション化します。

@Composable
fun AnimateContentSizeView() {
  var message by remember { mutableStateOf("Hello") }

  Column(
    Modifier.width(300.dp)
  ) {
    Box(
      modifier = Modifier
        .background(Color.LightGray)
        .animateContentSize()

    ) {
      Text(text = message)
    }
    Button(onClick = {
      message = message + " Compose"
    }) {
      Text(text = "add message Compose")
    }
  }

}

@Preview(showBackground = true)
@Composable
fun AnimateContentSizeViewPreview() {
  AnimationTheme {
    AnimateContentSizeView()
  }
}


// Crossfade
// https://developer.android.com/jetpack/compose/animation?hl=ja#crossfade

@Composable
fun CrossfadeView() {

  var currentPage by remember { mutableStateOf("A") }
  Column(
    Modifier.width(300.dp)
  ) {
    Box() {

      Crossfade(targetState = currentPage) { screen ->
        when (screen) {
          "A" -> Image(
            painter = painterResource(id = R.drawable.slide04),
            contentDescription = ""
          )

          "B" -> Image(
            painter = painterResource(id = R.drawable.slide09),
            contentDescription = ""
          )
        }
      }
    }
    Button(onClick = {
      currentPage = if (currentPage == "A") "B" else "A"
    }) {
      Text(text = "Change ${currentPage}")
    }
  }

}

@Preview(showBackground = true)
@Composable
fun CrossfadeViewPreview() {
  AnimationTheme {
    CrossfadeView()
  }
}

enum class BoxState {
  Small,
  Large
}

@Composable
fun UpdateTransitionView() {
  var currentState by remember { mutableStateOf(BoxState.Small) }
  val transition = updateTransition(currentState)

  val size by transition.animateDp { state ->
    when (state) {
      BoxState.Small -> 30.dp
      BoxState.Large -> 100.dp
    }
  }
  val borderWidth by transition.animateDp { state ->
    when (state) {
      BoxState.Small -> 1.dp
      BoxState.Large -> 4.dp
    }
  }
  val color by transition.animateColor { state ->
    when (state) {
      BoxState.Small -> Color.Green
      BoxState.Large -> Color.Magenta
    }

  }

  Column(
    modifier = Modifier.size(200.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Button(onClick = {
      currentState =
        if (currentState == BoxState.Small) BoxState.Large else BoxState.Small
    }) {
      Text(text = "updateTransition")
    }
    Image(
      modifier = Modifier
        .size(size = size)
        .clip(shape = CircleShape)
        .border(width = borderWidth, shape = CircleShape, color = color),
      painter = painterResource(id = R.drawable.slide04),
      contentDescription = ""
    )
  }
}

@Preview(showBackground = true)
@Composable
fun UpdateTransitionViewPreview() {
  AnimationTheme {
    UpdateTransitionView()
  }
}

// https://developer.android.com/jetpack/compose/animation?hl=ja#tooling
//ツールのサポート
//Android Studio は、アニメーション プレビューでの Transition の検査をサポートしています。
//
//遷移のフレーム単位のプレビュー
//遷移内の全アニメーションの値の検査
//初期状態とターゲット状態の間の遷移のプレビュー
//アニメーション プレビューを開始すると、プレビュー内の任意の遷移を実行できる [Animations] ペインが表示されます。
// 遷移とその個々のアニメーション値には、デフォルト名のラベルが付けられます。
// ラベルをカスタマイズするには、updateTransition 関数と animate* 関数で label パラメータを指定します。


// https://developer.android.com/jetpack/compose/animation?hl=ja#rememberinfinitetransition
// InfiniteTransition
// InfiniteTransition は 1 つ以上の子アニメーション（Transition など）を保持します。ただし、それらのアニメーションはコンポジションに入るとすぐに開始し、削除されない限り停止しません。InfiniteTransition のインスタンスは rememberInfiniteTransition で作成できます。子アニメーションは、animateColor、animatedFloat、または animatedValue を使用して追加できます。また、アニメーションの仕様を指定するには、infiniteRepeatable を指定する必要があります。


@Composable
fun RememberInfiniteTransitionView() {
  val infiniteTransition = rememberInfiniteTransition()
  val color by infiniteTransition.animateColor(
    initialValue = Color.Red,
    targetValue = Color.Green,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    )
  )
  Box(
    Modifier
      .fillMaxSize()
      .background(color)
  )
}

@Preview(showBackground = true)
@Composable
fun RememberInfiniteTransitionViewPreview() {
  AnimationTheme {
    RememberInfiniteTransitionView()
  }
}

// 低レベルアニメーション

// Animatable
//
@Composable
fun AnimatableView() {
  var ok by remember { mutableStateOf(true) }
  // Start out gray and animate to green/red based on `ok`
  val color = remember {
    // 初期値をグレーにすることができます
    // 最初にグレーのボックスが表示された後、すぐに緑色または赤色へのアニメーションが開始されます。
    Animatable(Color.Gray)
  }
  LaunchedEffect(ok) {
    color.animateTo(if (ok) Color.Green else Color.Red)
  }
  val composableScope = rememberCoroutineScope()
  Column() {
    Box(
      Modifier
        .size(300.dp)
        .background(color.value)
    )
    Button(onClick = { ok = !ok }) {
      Text("ok ${ok}")
    }
    Button(onClick = {
      // snapTo は、現在の値を直ちにターゲット値に設定します。
      // これは、アニメーション自体が唯一の信頼できる情報源ではなく、タップイベントなどの他の状態と同期する必要がある場合に便利です。
      composableScope.launch {
        color.snapTo(targetValue = Color.Magenta)
      }
    }) {
      Text(text = "snapTo")
    }
  }
}

@Preview(showBackground = true)
@Composable
fun AnimatableViewPreview() {
  AnimationTheme {
    AnimatableView()
  }
}

@Composable
fun EasingView() {

  var size by remember {
    mutableStateOf(0.5f)
  }
  val animationScale by animateFloatAsState(
    targetValue = size,
    animationSpec = tween(
      // アニメーションの継続時間
      durationMillis = 2000,
      // アニメーションの開始を延期する。
      delayMillis = 500,
      // 開始と終了の間を補間するために使用されるイージングカーブ
      easing = LinearOutSlowInEasing
    )
  )

  Box(
    modifier = Modifier
      .size(size = 300.dp),
    contentAlignment = Alignment.Center
  ) {
    Box(
      modifier = Modifier
        .scale(scale = animationScale)
        .size(size = 100.dp)
        .background(color = Color.Magenta)
        .clickable {
          size = if (size == 2f) 0.5f else 2f
        }
    ) {
    }
  }
}

@Preview(showBackground = true)
@Composable
fun EasingViewPreview() {
  AnimationTheme {
    EasingView()
  }
}

// easing
// Compose には、ほとんどのユースケースに対応できる組み込みの Easing 関数がいくつか用意されています。
// FastOutSlowInEasing
// LinearOutSlowInEasing
// FastOutLinearEasing
// LinearEasing
// CubicBezierEasing