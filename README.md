# EasyMark

EasyMark is an open source project, aim at providing a markdown editor and viewer for better performance. This project used to be one part of my another open source app [MarkNote](https://github.com/Shouheng88/MarkNote). If you are interested in building an markdown grammed note-taking application, you can refer to that project at [https://github.com/Shouheng88/MarkNote](https://github.com/Shouheng88/MarkNote).

## Functions

Here is an sample usage of this project

<div style="display:flex;" id="target">
<img  src="images/Screenshot_20181125-220420.jpg" width="19%" >
<img style="margin-left:10px;" src="images/Screenshot_20181125-220430.jpg" width="19%" >
<img style="margin-left:10px;" src="images/Screenshot_20181125-220438.jpg" width="19%" >
<img style="margin-left:10px;" src="images/Screenshot_20181125-220446.jpg" width="19%" >
<img style="margin-left:10px;" src="images/Screenshot_20181125-220454.jpg" width="19%" >
</div>

The function including now:

For editor:

1. Based on the AppCompactEdit;
2. Not real-time parsing and display, only display your raw markdown text;
3. You can add your own functions by implementing the interface provided.
4. Provided cool fast scroller

For viewer:

1. Based on the WebView, for it has a better performance and many cool features;
2. Support html;
3. Support many basic markdown grammers;
4. Support MathJax;
5. Provided the interface to handle the image and link click event;
6. Provided the interface for custom css
7. Provided cool fast scroller

## Usage

First, include my maven center,

    maven { url "https://shouheng.bintray.com/Android" }

Then add the dependence in your proejct,

    implementation 'me.shouheng.easymark:easymark:0.0.1'

For more, you can refer to the sample project.

## Contact

If you have any good idea please contact me at [shouheng2015@gmail.com](mailto:shuoheng2015@gmail.com)
