/*
 * InLab Software Product
 *
 * Copyright (c) 2020, InLab Software, LLC. All rights reserved.
 * Use is subject to license terms.
 *
 * http://www.inlabsoft.com/products/product/license
 */

import com.inlabsoft.sen.shared.logging.LoggerFactory;
import com.inlabsoft.sen.shared.logging.internal.jdk.LoggerFactoryJdk;
import com.inlabsoft.sen.shared.logging.internal.nil.LoggerFactoryNil;

module sen.shared.logging {

     exports com.inlabsoft.sen.shared.logging;
     exports com.inlabsoft.sen.shared.logging.internal.jdk to sen.shared.core;
     exports com.inlabsoft.sen.shared.logging.internal.nil to sen.shared.core;

    requires sen.shared.core;

    requires java.logging;

        uses LoggerFactory;

    provides LoggerFactory
        with LoggerFactoryJdk, LoggerFactoryNil;

}