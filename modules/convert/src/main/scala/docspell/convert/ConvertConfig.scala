/*
 * Copyright 2020 Docspell Contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package docspell.convert

import docspell.convert.extern.OcrMyPdfConfig
import docspell.convert.extern.{TesseractConfig, UnoconvConfig, WkHtmlPdfConfig}
import docspell.convert.flexmark.MarkdownConfig

case class ConvertConfig(
    chunkSize: Int,
    convertedFilenamePart: String,
    maxImageSize: Int,
    markdown: MarkdownConfig,
    wkhtmlpdf: WkHtmlPdfConfig,
    tesseract: TesseractConfig,
    unoconv: UnoconvConfig,
    ocrmypdf: OcrMyPdfConfig
)
