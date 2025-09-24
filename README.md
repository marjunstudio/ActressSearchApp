# 女優検索アプリ 最終仕様書

## 1. プロジェクト概要

### 1.1 アプリケーション概要
DMMアフィリエイトAPIを活用した女優検索Androidアプリケーション

### 1.2 基本情報
- **プロジェクト名**: ActressSearch
- **ターゲットユーザー**: 動画好きな一般ユーザー
- **主な用途**: 女優のプロフィール確認と関連作品の参照
- **プラットフォーム**: Android（Kotlin）
- **UI Framework**: Jetpack Compose + Material Design 3 Expressive
- **アーキテクチャ**: MVVM + Repository Pattern
- **開発方針**: モダンなAndroid開発手法を採用

## 2. 技術仕様

### 2.1 開発環境
- **言語**: Kotlin
- **最小SDK**: API 24 (Android 7.0)
- **ターゲットSDK**: API 34 (Android 14)
- **コンパイルSDK**: API 34
- **IDE**: Android Studio
- **Gradle**: Kotlin DSL

### 2.2 アーキテクチャ構成
```
┌─────────────────────────────────────┐
│           Presentation Layer        │
│  ┌─────────────────────────────────┐ │
│  │        Jetpack Compose          │ │
│  │     (Material Design 3)         │ │
│  └─────────────────────────────────┘ │
│  ┌─────────────────────────────────┐ │
│  │          ViewModel              │ │
│  │      (State Management)         │ │
│  └─────────────────────────────────┘ │
└─────────────────────────────────────┘
                    │
┌─────────────────────────────────────┐
│            Domain Layer             │
│  ┌─────────────────────────────────┐ │
│  │           UseCase               │ │
│  └─────────────────────────────────┘ │
│  ┌─────────────────────────────────┐ │
│  │      Repository Interface       │ │
│  └─────────────────────────────────┘ │
└─────────────────────────────────────┘
                    │
┌─────────────────────────────────────┐
│             Data Layer              │
│  ┌─────────────────────────────────┐ │
│  │    Repository Implementation    │ │
│  └─────────────────────────────────┘ │
│  ┌─────────────────────────────────┐ │
│  │        Remote Data Source       │ │
│  │      (Retrofit + OkHttp)        │ │
│  └─────────────────────────────────┘ │
│  ┌─────────────────────────────────┐ │
│  │        Local Data Source        │ │
│  │           (Room DB)             │ │
│  └─────────────────────────────────┘ │
└─────────────────────────────────────┘
```

### 2.3 使用ライブラリ・技術スタック
#### Core Libraries
- **UI**: Jetpack Compose BOM 2024.02.00
- **Material Design**: Material3 Compose
- **Navigation**: Navigation Compose
- **Lifecycle**: ViewModel Compose

#### Dependency Injection
- **DI**: Dagger Hilt

#### Asynchronous Processing
- **Coroutines**: Kotlin Coroutines
- **Reactive**: StateFlow, SharedFlow

#### Network & Serialization
- **HTTP Client**: Retrofit2 + OkHttp3
- **JSON Parsing**: Moshi (高パフォーマンス)
- **Image Loading**: Coil (Compose対応、高パフォーマンス)

#### Local Database
- **Database**: Room
- **Preferences**: DataStore (SharedPreferences replacement)

#### Development & Testing
- **Testing**: JUnit, Mockito, Compose Testing
- **Code Quality**: Detekt, ktlint

### 2.4 プロジェクト構造
```
app/
├── src/
│   ├── main/
│   │   ├── java/com/actresssearch/
│   │   │   ├── data/
│   │   │   │   ├── local/
│   │   │   │   │   ├── dao/
│   │   │   │   │   ├── database/
│   │   │   │   │   └── entity/
│   │   │   │   ├── remote/
│   │   │   │   │   ├── api/
│   │   │   │   │   ├── dto/
│   │   │   │   │   └── interceptor/
│   │   │   │   └── repository/
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   ├── repository/
│   │   │   │   └── usecase/
│   │   │   ├── presentation/
│   │   │   │   ├── ui/
│   │   │   │   │   ├── screen/
│   │   │   │   │   ├── component/
│   │   │   │   │   └── theme/
│   │   │   │   └── viewmodel/
│   │   │   └── di/
│   │   └── res/
│   └── test/ & androidTest/
```

## 3. API仕様

### 3.1 DMMアフィリエイト女優検索API
- **ベースURL**: [DMMアフィリエイトAPIのベースURL]
- **認証方式**: APIキー + アフィリエイトID
- **通信方式**: HTTPS
- **データ形式**: JSON

### 3.2 認証情報管理
```kotlin
// local.properties (Git管理対象外)
dmm.api.key="YOUR_API_KEY"
dmm.affiliate.id="YOUR_AFFILIATE_ID"

// BuildConfig経由で取得
val apiKey = BuildConfig.DMM_API_KEY
val affiliateId = BuildConfig.DMM_AFFILIATE_ID
```

### 3.3 APIパラメータ
| パラメータ | 必須 | 説明 |
|-----------|------|------|
| api_id | ○ | APIキー |
| affiliate_id | ○ | アフィリエイトID |
| initial | - | 50音でのイニシャル |
| keyword | - | キーワード検索 |
| gte_bust | - | バスト以上 |
| lte_bust | - | バスト以下 |
| gte_waist | - | ウエスト以上 |
| lte_waist | - | ウエスト以下 |
| gte_hip | - | ヒップ以上 |
| lte_hip | - | ヒップ以下 |
| gte_height | - | 身長以上 |
| lte_height | - | 身長以下 |
| gte_birthday | - | 生年月日以降 |
| lte_birthday | - | 生年月日以前 |
| hits | - | 取得件数（デフォルト20） |
| offset | - | 開始位置 |
| sort | - | ソート順（デフォルト：name） |

## 4. 機能仕様

### 4.1 検索機能
#### 4.1.1 イニシャル検索
- **50音検索**: あ、か、さ、た、な、は、ま、や、ら、わ
- **アルファベット検索**: A、B、C、D、E、F、G、H、I、J、K、L、M、N、O、P、Q、R、S、T、U、V、W、X、Y、Z
- **UI**: ボタンタイル形式での選択

#### 4.1.2 キーワード検索
- **入力方式**: 検索バーでの自由入力
- **検索対象**: 女優名（部分一致）
- **リアルタイム検索**: 入力完了後の自動検索

#### 4.1.3 詳細検索（フィルター機能）
**体型フィルター**:
- バスト（〇〇cm以上 / 〇〇cm以下）
- ウエスト（〇〇cm以上 / 〇〇cm以下）
- ヒップ（〇〇cm以上 / 〇〇cm以下）
- 身長（〇〇cm以上 / 〇〇cm以下）

**年代フィルター**:
- 生年月日（〇〇年以降 / 〇〇年以前）

#### 4.1.4 ソート機能
- **デフォルト**: 名前昇順
- **対応**: 将来的に他のソート条件も対応予定

#### 4.1.5 ページング機能
- **方式**: 無限スクロール
- **1ページ取得件数**: 20件
- **読み込みトリガー**: リスト終端に近づいた時点
- **ローディング表示**: 読み込み中のインジケーター表示

### 4.2 女優詳細表示機能
#### 4.2.1 プロフィール画像
- **大きい画像**: メイン表示用
- **アスペクト比**: API提供画像に準拠
- **ローディング**: プレースホルダー表示

#### 4.2.2 基本情報表示
- **名前**: 漢字表記
- **ふりがな**: ひらがな表記
- **生年月日**: yyyy/MM/dd形式
- **血液型**: A、B、O、AB型
- **出身地**: 都道府県
- **趣味**: フリーテキスト

#### 4.2.3 体型情報表示
- **バスト**: 数値 + カップサイズ（例：B88(D)）
- **ウエスト**: 数値（例：W58）
- **ヒップ**: 数値（例：H85）
- **身長**: 数値（例：165cm）

#### 4.2.4 関連作品リンク
- **デジタル作品**: 外部ブラウザでリンク先へ遷移
- **月額サービス**: 外部ブラウザでリンク先へ遷移
- **単品作品**: 外部ブラウザでリンク先へ遷移

### 4.3 お気に入り機能
#### 4.3.1 お気に入り登録・解除
- **登録方式**: 詳細画面でのハートボタンタップ
- **状態表示**: 塗りつぶし（登録済み）/アウトライン（未登録）
- **保存方式**: ローカルDB（Room）にIDのみ保存

#### 4.3.2 お気に入り一覧
- **表示方式**: 検索結果と同様のリスト表示
- **データ取得**: 保存されたIDに基づくAPI再取得
- **削除機能**: スワイプ or 長押しでの削除

## 5. 画面設計

### 5.1 画面遷移図
```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│    検索画面   │────│   詳細画面    │────│  お気に入り  │
│             │    │             │    │    画面     │
│ ・50音検索   │    │ ・プロフィール │    │             │
│ ・キーワード  │    │ ・体型情報    │    │ ・一覧表示   │
│ ・詳細検索   │    │ ・関連作品    │    │ ・削除機能   │
│ ・結果一覧   │    │ ・お気に入り  │    │             │
└─────────────┘    └─────────────┘    └─────────────┘
```

### 5.2 メイン検索画面
```
┌─────────────────────────────────────┐
│ ┌─────────────────────────────────┐ │
│ │        🔍 女優名で検索           │ │
│ └─────────────────────────────────┘ │
├─────────────────────────────────────┤
│    50音    │  キーワード │   詳細     │
├─────────────────────────────────────┤
│ [あ] [か] [さ] [た] [な] [は] [ま]  │
│ [や] [ら] [わ] [A] [B] [C] [D]     │
├─────────────────────────────────────┤
│ ┌─────┐ 田中美奈子                  │
│ │画像 │ 身長:165cm バスト:88cm      │
│ └─────┘ 1985/03/15 A型 東京都      │
├─────────────────────────────────────┤
│ ┌─────┐ 佐藤花子                   │
│ │画像 │ 身長:160cm バスト:85cm      │
│ └─────┘ 1990/07/22 B型 大阪府      │
├─────────────────────────────────────┤
│          ⟳ 読み込み中...            │
└─────────────────────────────────────┘
```

### 5.3 詳細検索フィルター画面
```
┌─────────────────────────────────────┐
│           詳細検索フィルター          │
├─────────────────────────────────────┤
│ 体型情報                            │
│ バスト     [_____] cm 以上          │
│           [_____] cm 以下          │
│ ウエスト   [_____] cm 以上          │
│           [_____] cm 以下          │
│ ヒップ     [_____] cm 以上          │
│           [_____] cm 以下          │
│ 身長      [_____] cm 以上          │
│           [_____] cm 以下          │
├─────────────────────────────────────┤
│ 年代情報                            │
│ 生年月日   [____] 年 以降           │
│           [____] 年 以前           │
├─────────────────────────────────────┤
│      [クリア]        [検索]          │
└─────────────────────────────────────┘
```

### 5.4 女優詳細画面
```
┌─────────────────────────────────────┐
│              ← 戻る      ♡          │
├─────────────────────────────────────┤
│                                     │
│         [大きいプロフィール画像]      │
│                                     │
├─────────────────────────────────────┤
│ 田中美奈子（たなかみなこ）           │
│ 1985/03/15  A型                    │
│ 東京都出身                          │
│ 趣味：読書、映画鑑賞                │
├─────────────────────────────────────┤
│ 体型情報                            │
│ 身長：165cm                        │
│ バスト：B88(D) ウエスト：W58        │
│ ヒップ：H85                        │
├─────────────────────────────────────┤
│ 関連作品                            │
│ ┌─────────────────────────────────┐ │
│ │        📱 デジタル作品           │ │
│ └─────────────────────────────────┘ │
│ ┌─────────────────────────────────┐ │
│ │        📺 月額サービス           │ │
│ └─────────────────────────────────┘ │
│ ┌─────────────────────────────────┐ │
│ │        💿 単品作品              │ │
│ └─────────────────────────────────┘ │
└─────────────────────────────────────┘
```

### 5.5 お気に入り画面
```
┌─────────────────────────────────────┐
│           お気に入り一覧             │
├─────────────────────────────────────┤
│ ┌─────┐ 田中美奈子            ♡    │
│ │画像 │ 身長:165cm バスト:88cm      │
│ └─────┘ 1985/03/15 A型 東京都      │
├─────────────────────────────────────┤
│ ┌─────┐ 佐藤花子              ♡    │
│ │画像 │ 身長:160cm バスト:85cm      │
│ └─────┘ 1990/07/22 B型 大阪府      │
├─────────────────────────────────────┤
│              ︙                    │
│          (空の状態)                 │
│     まだお気に入りがありません        │
│                                     │
└─────────────────────────────────────┘
```

## 6. データモデル設計

### 6.1 APIレスポンス（Remote DTO）
```kotlin
@JsonClass(generateAdapter = true)
data class ActressApiResponse(
    val request: RequestInfo,
    val result: ResultInfo
)

@JsonClass(generateAdapter = true)
data class RequestInfo(
    val parameters: Map<String, String>
)

@JsonClass(generateAdapter = true)
data class ResultInfo(
    val status: Int,
    @Json(name = "result_count") val resultCount: Int,
    @Json(name = "total_count") val totalCount: Int,
    @Json(name = "first_position") val firstPosition: Int,
    val actress: List<ActressDto>
)

@JsonClass(generateAdapter = true)
data class ActressDto(
    val id: String,
    val name: String,
    val ruby: String,
    val bust: String?,
    val cup: String?,
    val waist: String?,
    val hip: String?,
    val height: String?,
    val birthday: String?, // yyyy-MM-dd
    @Json(name = "blood_type") val bloodType: String?,
    val hobby: String?,
    val prefectures: String?,
    @Json(name = "imageURL") val imageUrl: ImageUrls,
    @Json(name = "listURL") val listUrl: ListUrls
)

@JsonClass(generateAdapter = true)
data class ImageUrls(
    val small: String,
    val large: String
)

@JsonClass(generateAdapter = true)
data class ListUrls(
    val digital: String,
    val monthly: String,
    val mono: String
)
```

### 6.2 ドメインモデル
```kotlin
data class Actress(
    val id: String,
    val name: String,
    val ruby: String,
    val bust: String?,
    val cup: String?,
    val waist: String?,
    val hip: String?,
    val height: String?,
    val birthday: String?,
    val bloodType: String?,
    val hobby: String?,
    val prefectures: String?,
    val imageUrl: ImageUrls,
    val listUrl: ListUrls,
    val isFavorite: Boolean = false
)
```

### 6.3 検索パラメータ
```kotlin
data class ActressSearchParams(
    val initial: String? = null,
    val keyword: String? = null,
    val gteBust: Int? = null,
    val lteBust: Int? = null,
    val gteWaist: Int? = null,
    val lteWaist: Int? = null,
    val gteHip: Int? = null,
    val lteHip: Int? = null,
    val gteHeight: Int? = null,
    val lteHeight: Int? = null,
    val gteBirthday: String? = null,
    val lteBirthday: String? = null,
    val hits: Int = 20,
    val offset: Int = 1,
    val sort: String = "name"
)
```

### 6.4 お気に入りエンティティ（Room）
```kotlin
@Entity(tableName = "favorite_actresses")
data class FavoriteActressEntity(
    @PrimaryKey val actressId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Dao
interface FavoriteActressDao {
    @Query("SELECT * FROM favorite_actresses ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteActressEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteActressEntity)
    
    @Delete
    suspend fun removeFavorite(favorite: FavoriteActressEntity)
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_actresses WHERE actressId = :actressId)")
    suspend fun isFavorite(actressId: String): Boolean
}
```

## 7. UI State Management

### 7.1 検索画面UIState
```kotlin
data class ActressSearchUiState(
    val isLoading: Boolean = false,
    val actresses: List<Actress> = emptyList(),
    val searchParams: ActressSearchParams = ActressSearchParams(),
    val errorMessage: String? = null,
    val hasMore: Boolean = true,
    val currentPage: Int = 1,
    val totalCount: Int = 0,
    val isLoadingMore: Boolean = false
)

sealed class ActressSearchEvent {
    data class Search(val params: ActressSearchParams) : ActressSearchEvent()
    object LoadMore : ActressSearchEvent()
    object ClearError : ActressSearchEvent()
    data class UpdateSearchParams(val params: ActressSearchParams) : ActressSearchEvent()
}
```

### 7.2 詳細画面UIState
```kotlin
data class ActressDetailUiState(
    val actress: Actress? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isFavorite: Boolean = false
)

sealed class ActressDetailEvent {
    data class LoadActress(val id: String) : ActressDetailEvent()
    object ToggleFavorite : ActressDetailEvent()
    object ClearError : ActressDetailEvent()
}
```

### 7.3 お気に入り画面UIState
```kotlin
data class FavoriteUiState(
    val favoriteActresses: List<Actress> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed class FavoriteEvent {
    object LoadFavorites : FavoriteEvent()
    data class RemoveFavorite(val actressId: String) : FavoriteEvent()
    object ClearError : FavoriteEvent()
}
```

## 8. エラーハンドリング

### 8.1 エラー種別
```kotlin
sealed class ApiError : Exception() {
    object NetworkError : ApiError()
    object TimeoutError : ApiError()
    data class HttpError(val code: Int, val message: String) : ApiError()
    object AuthenticationError : ApiError()
    object UnknownError : ApiError()
    
    fun getDisplayMessage(): String = when (this) {
        NetworkError -> "ネットワーク接続を確認してください"
        TimeoutError -> "通信がタイムアウトしました"
        is HttpError -> "サーバーエラーが発生しました（エラーコード: $code）"
        AuthenticationError -> "認証に失敗しました"
        UnknownError -> "予期しないエラーが発生しました"
    }
}
```

### 8.2 エラー表示
```kotlin
@Composable
fun ErrorDialog(
    error: ApiError?,
    onDismiss: () -> Unit,
    onRetry: () -> Unit
) {
    error?.let {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("エラー") },
            text = { Text(it.getDisplayMessage()) },
            confirmButton = {
                TextButton(onClick = onRetry) {
                    Text("再試行")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("キャンセル")
                }
            }
        )
    }
}
```

## 9. パフォーマンス最適化

### 9.1 画像読み込み最適化
```kotlin
// Coil設定
@Composable
fun ActressImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .build(),
        placeholder = painterResource(R.drawable.placeholder_actress),
        error = painterResource(R.drawable.error_actress),
        modifier = modifier,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop
    )
}
```

### 9.2 リスト最適化
```kotlin
// LazyColumn最適化
@Composable
fun ActressList(
    actresses: List<Actress>,
    onActressClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(
            items = actresses,
            key = { _, actress -> actress.id }
        ) { index, actress ->
            ActressListItem(
                actress = actress,
                onClick = { onActressClick(actress.id) }
            )
            
            // 無限スクロールトリガー
            if (index >= actresses.size - 5) {
                LaunchedEffect(Unit) {
                    onLoadMore()
                }
            }
        }
    }
}
```

## 10. セキュリティ・プライバシー

### 10.1 API認証情報保護
- BuildConfigによる環境変数管理
- ProGuardによるコード難読化
- デバッグビルドでの認証情報表示防止

### 10.2 通信セキュリティ
- HTTPS通信の徹底
- Certificate Pinning（オプション）
- Network Security Config設定

### 10.3 プライバシー
- ユーザーの個人情報収集なし
- 検索履歴のローカル保存なし
- お気に入りのみローカル保存

## 11. テスト戦略

### 11.1 テスト種別
```kotlin
// Unit Test例
@Test
fun `女優検索が成功した場合、結果が正しく返される`() = runTest {
    // Given
    val mockResponse = ActressApiResponse(...)
    whenever(apiService.searchActresses(...)).thenReturn(Response.success(mockResponse))
    
    // When
    val result = repository.searchActresses(searchParams)
    
    // Then
    assertTrue(result.isSuccess)
    assertEquals(mockResponse, result.getOrNull())
}

// UI Test例
@Test
fun 検索バーに入力して検索ボタンをタップすると検索が実行される() {
    composeTestRule.setContent {
        ActressSearchScreen(...)
    }
    
    composeTestRule
        .onNodeWithContentDescription("検索バー")
        .performTextInput("田中")
    
    composeTestRule
        .onNodeWithText("検索")
        .performClick()
    
    // 検索結果が表示されることを確認
    composeTestRule
        .onNodeWithText("田中美奈子")
        .assertIsDisplayed()
}
```

## 12. 実装フェーズ

### Phase 1: 基盤構築（2週間）
- [x] プロジェクト初期設定
- [x] 依存関係追加
- [x] アーキテクチャ基盤構築
- [x] データモデル作成
- [x] API Service実装
- [x] Repository実装

### Phase 2: 基本機能実装（3週間）
- [ ] メイン検索画面（50音・キーワード検索）
- [ ] 検索結果一覧表示
- [ ] 女優詳細画面
- [ ] 基本的なエラーハンドリング
- [ ] 無限スクロール実装

### Phase 3: 追加機能実装（2週間）
- [ ] お気に入り機能
- [ ] 詳細検索フィルター
- [ ] UI/UXの改善
- [ ] エラーハンドリング強化

### Phase 4: 品質向上・リリース準備（2週間）
- [ ] テストコード作成
- [ ] パフォーマンス最適化
- [ ] セキュリティ強化
- [ ] リリースビルド設定
- [ ] ストア申請準備

## 13. リリース・運用

### 13.1 配布
- **プラットフォーム**: Google Play Store
- **最小要件**: Android 7.0以上
- **対象デバイス**: スマートフォン・タブレット

### 13.2 運用・監視
- **クラッシュレポート**: Firebase Crashlytics
- **ユーザー分析**: Firebase Analytics（プライバシー配慮）
- **パフォーマンス監視**: Firebase Performance Monitoring

### 13.3 継続的改善
- ユーザーフィードバック収集
- 定期的なライブラリアップデート
- 新機能の企画・実装

---

この仕様書に基づいて、モダンで使いやすい女優検索アプリを開発していきます。