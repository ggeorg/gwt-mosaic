/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gwt.mosaic.client.wtk;

import gwt.mosaic.client.wtk.media.Image;

/**
 * Image view listener interface.
 */
public interface ImageViewListener {
    /**
     * Image view listener adapter.
     */
    public static class Adapter implements ImageViewListener {
        @Override
        public void imageChanged(ImageView imageView, Image previousImage) {
        }

        @Override
        public void asynchronousChanged(ImageView imageView) {
        }
    }

    /**
     * Called when an image view's image has changed.
     *
     * @param imageView
     * @param previousImage
     */
    public void imageChanged(ImageView imageView, Image previousImage);

    /**
     * Called when an image view's asynchronous flag has changed.
     *
     * @param imageView
     */
    public void asynchronousChanged(ImageView imageView);
}
