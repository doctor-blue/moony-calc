/*
   Copyright 2013 Paul LeBeau, Cave Rock Software Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.moony.calc.views

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.moony.androidsvg.RenderOptions
import com.moony.androidsvg.SVG
import com.moony.androidsvg.SVGParseException
import com.moony.calc.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.lang.reflect.Method

/**
 * SVGImageView is a View widget that allows users to include SVG images in their layouts.
 *
 *
 * It is implemented as a thin layer over `android.widget.ImageView`.
 *
 * <h2>XML attributes</h2>
 * <dl>
 * <dt>`svg`</dt>
 * <dd>A resource reference, or a file name, of an SVG in your application</dd>
 * <dt>`css`</dt>
 * <dd>Optional extra CSS to apply when rendering the SVG</dd>
</dl> *
 */
class SVGImageView : AppCompatImageView {
    private var svg: SVG? = null
    private val renderOptions = RenderOptions()

    companion object {
        private var setLayerTypeMethod: Method? = null

        init {
            try {
                setLayerTypeMethod =
                    View::class.java.getMethod(
                        "setLayerType", Integer.TYPE,
                        Paint::class.java
                    )
            } catch (e: NoSuchMethodException) { /* do nothing */
            }
        }
    }

    constructor(context: Context?) : super(context)
    constructor(
        context: Context?,
        attrs: AttributeSet
    ) : super(context, attrs, 0) {
        init(attrs, 0)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet, defStyle: Int) {
        if (isInEditMode) return
        val a = context.theme
            .obtainStyledAttributes(attrs, R.styleable.SVGImageView, defStyle, 0)
        try {
            // Check for css attribute
            val css = a.getString(R.styleable.SVGImageView_css)
            if (css != null) renderOptions.css(css)

            // Check whether svg attribute is a resourceId
            val resourceId = a.getResourceId(R.styleable.SVGImageView_svg, -1)
            if (resourceId != -1) {
                setImageResource(resourceId)
                return
            }

            // Check whether svg attribute is a string.
            // Could be a URL/filename or an SVG itself
            val url = a.getString(R.styleable.SVGImageView_svg)
            if (url != null) {
                val uri = Uri.parse(url)
                if (internalSetImageURI(uri)) return

                // Not a URL, so try loading it as an asset filename
                if (internalSetImageAsset(url)) return

                // Last chance, maybe there is an actual SVG in the string
                // If the SVG is in the string, then we will assume it is not very large, and thus doesn't need to be parsed in the background.
                setFromString(url)
            }
        } finally {
            a.recycle()
        }
    }

    /**
     * Directly set the SVG that should be rendered by this view.
     *
     * @param svg An `SVG` instance
     * @since 1.2.1
     */
    fun setSVG(svg: SVG?) {
        requireNotNull(svg) { "Null value passed to setSVG()" }
        this.svg = svg
        doRender()
    }

    /**
     * Directly set the SVG and the CSS.
     *
     * @param svg An `SVG` instance
     * @param css Optional extra CSS to apply when rendering
     * @since 1.3
     */
    fun setSVG(svg: SVG?, css: String?) {
        requireNotNull(svg) { "Null value passed to setSVG()" }
        this.svg = svg
        renderOptions.css(css)
        doRender()
    }

    /**
     * Directly set the CSS.
     *
     * @param css Extra CSS to apply when rendering
     * @since 1.3
     */
    fun setCSS(css: String?) {
        renderOptions.css(css)
        doRender()
    }

    /**
     * Load an SVG image from the given resource id.
     *
     * @param resourceId the id of an Android resource in your application
     */
    override fun setImageResource(resourceId: Int) {
        loadResource(context, resourceId)
    }

    /**
     * Load an SVG image from the given resource URI.
     *
     * @param uri the URI of an Android resource in your application
     */
    override fun setImageURI(uri: Uri?) {
        if (!internalSetImageURI(uri)) Log.e("SVGImageView", "File not found: $uri")
    }

    /**
     * Load an SVG image from the given asset filename.
     *
     * @param filename the file name of an SVG in the assets folder in your application
     */
    fun setImageAsset(filename: String) {
        if (!internalSetImageAsset(filename)) Log.e(
            "SVGImageView",
            "File not found: $filename"
        )
    }

    fun setImageFromStorage(path: String) {
        // /storage/emulated/0/book.svg
        try {
            val storageDir = File(
                path
            )
            val inputStream = FileInputStream(storageDir)
            loadUrl(inputStream)
        } catch (e: IOException) {
            Log.d(SVGImageView::class.java.simpleName, "Could not find SVG at: $path")
        }
    }

    //===============================================================================================
    /*
     * Attempt to set a picture from a Uri. Return true if it worked.
     */

    private fun internalSetImageURI(uri: Uri?): Boolean =
        try {
            val inputStream = context.contentResolver.openInputStream(uri!!)
            inputStream?.let {
                loadUrl(inputStream)
            }
            true
        } catch (e: FileNotFoundException) {
            false
        }


    private fun internalSetImageAsset(filename: String): Boolean =
        try {
            val inputStream = context.assets.open(filename)
            loadUrl(inputStream)
            true
        } catch (e: IOException) {
            false
        }


    private fun setFromString(url: String) =
        try {
            svg = SVG.getFromString(url)
            doRender()
        } catch (e: SVGParseException) {
            // Failed to interpret url as a resource, a filename, or an actual SVG...
            Log.e("SVGImageView", "Could not find SVG at: $url")
        }


    //===============================================================================================
    /*  @SuppressLint("StaticFieldLeak")
      private inner class LoadResourceTask internal constructor(
          private val context: Context,
          private val resourceId: Int
      ) :
          AsyncTask<Int?, Int?, SVG?>() {

          override fun onPostExecute(svg: SVG?) {
              this@SVGImageView.svg = svg
              doRender()
          }

          override fun doInBackground(vararg params: Int?): SVG? {
              try {
                  return SVG.getFromResource(context, resourceId)
              } catch (e: SVGParseException) {
                  Log.e(
                      "SVGImageView",
                      String.format("Error loading resource 0x%x: %s", resourceId, e.message)
                  )
              }
              return null
          }

      }*/

    private fun loadResource(context: Context, resourceId: Int) =
        GlobalScope.launch(Dispatchers.IO) {
            try {
                svg = SVG.getFromResource(context, resourceId)
                withContext(Dispatchers.Main) {
                    doRender()
                }
            } catch (e: SVGParseException) {
                Log.e(
                    "SVGImageView",
                    String.format("Error loading resource 0x%x: %s", resourceId, e.message)
                )
            }
        }


    private fun loadUrl(inputStream: InputStream) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                this@SVGImageView.svg = SVG.getFromInputStream(inputStream)
                withContext(Dispatchers.Main) {
                    doRender()
                }
            } catch (e: SVGParseException) {
                Log.d(SVGImageView::class.java.simpleName, "Parse error loading URI: " + e.message)
            } finally {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    Log.d(SVGImageView::class.java.simpleName, e.toString())
                }
            }
        }
    }

    /*  @SuppressLint("StaticFieldLeak")
      private inner class LoadURITask : AsyncTask<InputStream?, Int?, SVG?>() {
          override fun doInBackground(vararg params: InputStream?): SVG? {
              try {
                  return SVG.getFromInputStream(params[0])
              } catch (e: SVGParseException) {
                  Log.e("VGImageView", "Parse error loading URI: " + e.message)
              } finally {
                  try {
                      params[0]!!.close()
                  } catch (e: IOException) {  //do nothing
                  }
              }
              return null
          }

          override fun onPostExecute(svg: SVG?) {
              this@SVGImageView.svg = svg
              doRender()
          }
      }*/

    //===============================================================================================
    /*
     * Use reflection to call an API 11 method from this library (which is configured with a minSdkVersion of 8)
     */
    private fun setSoftwareLayerType() {
        if (setLayerTypeMethod != null)
            try {
                val layerTypeSoftware =
                    View::class.java.getField("LAYER_TYPE_SOFTWARE")
                        .getInt(View(context))
                setLayerTypeMethod!!.invoke(
                    this,
                    layerTypeSoftware,
                    null
                )
            } catch (e: Exception) {
                Log.w("SVGImageView", "Unexpected failure calling setLayerType", e)
            }
    }

    private fun doRender() {
        if (svg == null) return
        val picture = svg!!.renderToPicture(renderOptions)
        setSoftwareLayerType()
        setImageDrawable(PictureDrawable(picture))
    }
}