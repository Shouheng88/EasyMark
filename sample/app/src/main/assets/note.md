EasyMark
===========================

This [md](id:readme) file is used to test all kinds of markdown grammars for the open sources project [EasyMark](https://github.com/Shouheng88/EasyMark). EasyMark is an open source project, aim at providing a markdown editor and viewer for better performance. This project used to be one part of my another open source app MarkNote. If you are interested in building an markdown note-taking application, you can refer to that project at [https://github.com/Shouheng88/MarkNote](https://github.com/Shouheng88/MarkNote).

-- by WngShhng (shouheng2015@gmail.com)

****

### 1. Horizontal line

Below are three kinds of horizontal lines:

***
---
___


### 2. Title

You can use the `#` to define 6 different kinds of title like:

# Title 1
## Title 2  
### Title 3  
#### Title 4  
##### Title 5  
###### Title 6  

### 3. Text 

This line is used to show a normal text.

    Use 1 tab or 4 spaces in front of a line can get a code block.
    You can also use the tab or sapces in another line, which will also be included in the same code block.

```
Another way to get a code block is to put your text between three ` symbols
```

To use an inline code, you need only put your text between two symbols, like `linux` `Network` `Socket` `epoll`, etc.

### 4. Change the line

You have two ways to change your lines,  
the first is, to add two sapces to the end of your text.

Another is to add a space line between two lines. The second method may make a big space between two lines.

### 5. Italic Bold and Strike

|Grammar|Effect|
|----|-----|
|`*Italic 1*`|*Italic 1*|
|`_Italic 2_`| _Italic 2_|
|`**Bold 1**`|**Bold 1**|
|`__Bold 2__`|__Bold 2__|
|`~~Strike through~~`|~~Strike through~~|
|`***Italic Bold 1***`|***Italic Bold 1***|
|`___Italic Bold 2___`|___Italic Bold 2___|
|`***~~Italic Bold and Strike through 1~~***`|***~~Italic Bold and Strike through 1~~***|
|`~~***Italic Bold and Strike through 2***~~`|~~***Italic Bold and Strike through 2***~~|

### 6. Image

The basic format of using the image in markdown is:

```
![alt](URL title)
```

- alt is the text to display when the image failed to load
- title is the text to display when the mosue hover on the image
- URL is the url of an image, relative or absolute

![Sample](https://github.com/Shouheng88/MarkNote/blob/master/resources/images/app.png "MarkNote Logo")

### 7. Link

The link in markdown is similar to the image in markdown, except that there is no `!` in the front:

```
[alt](URL title)
```

- alt is the text to display
- title is the text to display when the mosue hover on the link
- URL is the url of an link, relative or absolute

Here is an example:

[Github](https://github.com "Build software better, together.")

Also your can use the image link,

[![Sample](https://github.com/Shouheng88/MarkNote/blob/master/resources/images/app.png "MarkNote Logo")](https://github.com/Shouheng88/MarkNote)

### 8. Anchor

If fact, every kind of title is an anchor, for example : [To README](#readme).

### 9. List

The list has three types, you can combine them to get your desired effect:

* Author: WngShhng
- Gamil: shouehng2015@gmail.com
* Github: https://github.com/Shouheng88

### 10. CheckBox

You can add checkbox in your markdown, by using `- []` and `- [x]`

- [x] Requirement
- [ ] Final

### 11. Quote

You can use `>` to add quote text in your markdown:

> This [md](id:readme) file is used to test all kinds of markdown grammars for the open sources project [EasyMark](https://github.com/Shouheng88/EasyMark). EasyMark is an open source project, aim at providing a markdown editor and viewer for better performance. This project used to be one part of my another open source app MarkNote. If you are interested in building an markdown note-taking application, you can refer to that project at [https://github.com/Shouheng88/MarkNote](https://github.com/Shouheng88/MarkNote).

### 12. Code style

Add your language name behind the code bolck symbols can get the code style of the specified the languge:

```Java
public static void main(String[]args){} //Java
```

```c
int main(int argc, char *argv[]) //C
```

```Bash
echo "hello GitHub" #Bash
```

```javascript
document.getElementById("myH1").innerHTML="Welcome to my Homepage"; //javascipt
```

```cpp
string &operator+(const string& A,const string& B) //cpp
```

### 13. Table

Here is a simple sample for table in markdown:

| Header 1  | Header 2|
| --------- | --------|
| Cell (1, 1)  | Cell (1, 2) |
| Cell (2, 1)  | Cell (2, 2) |

Also, you can specify the align type of your text in the cell of table:

#### Align

For the example below we can see that we could get the align effect by adding the `:` to the dash line under header row:

| Align left | Align center  | Align Right |
| :------------ |:---------------:| -----:|
| col 3 is      | some wordy text | $1600 |
| col 2 is      | centered        |   $12 |
| zebra stripes | are neat        |    $1 |

Of course, you can use the effects combinations:

| Name | Description |
| ------------- | ----------- |
| Help      | ~~Display the~~ help window.|
| Close     | _Closes_ a window     |

### 14. MathJax

Below is some test of MathJax:

$\sum_{i=0}^N\int_{a}^{b}g(t,i)\text{d}t$
$$\sum_{i=0}^N\int_{a}^{b}g(t,i)\text{d}t$$
$$C_n^2$$
$$\vec a$$
$$\overrightarrow{xy}$$
$$\mathbb{A}$$
$$10^10+10^{10}$$
$$\left(\frac{x}{y}\right)$$
$$\int_0^\infty{fxdx}$$
$$\sqrt[x]{y}$$
$$\frac{公式1}{公式2}$$
$$\begin{matrix}
1&0&0\\
0&1&0\\
0&0&1\\
\end{matrix}$$
$$\sin x，\ln x，\max(A,B,C)$$
$$\begin{bmatrix}
{a_{11}}&{a_{12}}&{\cdots}&{a_{1n}}\\
{a_{21}}&{a_{22}}&{\cdots}&{a_{2n}}\\
{\vdots}&{\vdots}&{\ddots}&{\vdots}\\
{a_{m1}}&{a_{m2}}&{\cdots}&{a_{mn}}\\
\end{bmatrix}$$
$$\begin{array}{c|lll}
{↓}&{a}&{b}&{c}\\
\hline
{R_1}&{c}&{b}&{a}\\
{R_2}&{b}&{c}&{c}\\
\end{array}$$
$$\begin{cases}
a_1x+b_1y+c_1z=d_1\\
a_2x+b_2y+c_2z=d_2\\
a_3x+b_3y+c_3z=d_3\\
\end{cases}
$$

### 15. Inner HTML

You can use the HTML in markdown:

<div style="display:flex;" id="target">
<img  src="https://github.com/Shouheng88/EasyMark/raw/master/images/Screenshot_20181125-220420.jpg" width="32%" >
<img style="margin-left:10px;" src="https://github.com/Shouheng88/EasyMark/raw/master/images/Screenshot_20181125-220430.jpg" width="32%" >
<img style="margin-left:10px;" src="https://github.com/Shouheng88/EasyMark/raw/master/images/Screenshot_20181125-220438.jpg" width="32%" >
</div>

The markdown text above is used to show and test the effect of markdown. If you have any question, or you are interested in our project, you can view our website at https://github.com/Shouheng88/EasyMark or directly send me an Email: shouheng2015@gamil.com.