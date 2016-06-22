#navigator

[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0) [![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)

Navigator 

- Easy to navigate between fragments from Activity and Fragment
- Annotation create a easy and clearly way to navigate from other classes
 
## Usage

Extends `NavigatorFragmentActivity` or `NavigatorAppCompatActivity` and `NavigatorFragment`

**In Activity**

```java
getNavigator().openFragment(CharacterFragment.newInstance(ch), R.id.content, true,
                            LayoutType.REPLACE, animEnter, animExit, animPopEnter, animPopExit);
```

**In Fragment**

Open a fragment like the way activity open (same as above but from a fragment)
```java
getRootNavigator().openFragment(fragment, R.id.content, true, LayoutType.ADD,
                            animEnter, animExit, animPopEnter, animPopExit);
```

Open a nested fragment
```java
getOwnNavigator().openFragment(fragment, R.id.sub_content, true, LayoutType.ADD,
                            animEnter, animExit, animPopEnter, animPopExit);
```

**In Other**

Mark annotation `@HasNavigator` to a variable that contains Navigator object
```java
@HasNavigator
ActivityViewModel viewModel;
```
Navigator object (in ActivityViewModel) can be one of `@RootNavigator, @ParentNavigator, @OwnNavigator`
```java
@RootNavigator
Navigator rootNavigator;
```
And same as above, open a fragment
```java
rootNavigator.openFragment(fragment, R.id.content, true, LayoutType.ADD,
                            animEnter, animExit, animPopEnter, animPopExit);
```

## Sample

Dive deep with the [sample](sample)

## Build integration 

Gradle:

```gradle
buildscript {
  dependencies {
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
  }
}

apply plugin: 'com.neenbedankt.android-apt'

dependencies {
  compile 'io.fruitful.navigator:navigator:0.3'
  apt 'io.fruitful.navigator:navigator-processor'
}
```


## Advanced

### Dispatcher

This dispatches an `Activity` or `Fragment` has navigators like our build-in `NavigatorFragmentActivity` or `NavigatorAppCompatActivity` and `NavigatorFragment`
#### Activity
```java
public class OurActivity extends AnotherActivity implements NavigatorActivityInterface {

    private NavigatorActivityDispatcher<NavigatorAppCompatActivity> dispatcher = new NavigatorActivityDispatcher<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dispatcher.onCreate(this);
    }

    @Override
    public boolean isStateSaved() {
        return dispatcher.isStateSaved();
    }

    @Override
    public Navigator getNavigator() {
        return dispatcher.getNavigator();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispatcher.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dispatcher.onBackPressed();
    }
}
```

#### Fragment

```java
public class OurFragment extends Fragment implements NavigatorFragmentInterface {

    private NavigatorFragmentDispatcher<OurFragment> dispatcher = new NavigatorFragmentDispatcher<>();

    /**
     * @return true if Fragment need interact with back command e.g: hide the popup layout,
     * hide search layout,... and more
     */
    public boolean handleBackIfNeeded() {
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dispatcher.onActivityCreated(this);
    }

    @Override
    public Navigator getRootNavigator() {
        return dispatcher.getRootNavigator();
    }

    @Override
    public Navigator getParentNavigator() {
        return dispatcher.getParentNavigator();
    }

    @Override
    public Navigator getOwnNavigator() {
        return dispatcher.getOwnNavigator();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dispatcher.onDestroy();
    }
}
```