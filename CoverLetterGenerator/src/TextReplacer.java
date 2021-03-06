import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class TextReplacer {
	private String searchValue;
	private String replacement;

	public TextReplacer(String searchValue, String replacement) {
		this.searchValue = searchValue;
		this.replacement = replacement;
	}

	public void replace(XWPFDocument document) {
		List<XWPFParagraph> paragraphs = document.getParagraphs();

		for (XWPFParagraph xwpfParagraph : paragraphs) {
			replace(xwpfParagraph);
		}
	}

	private void replace(XWPFParagraph paragraph) {
		if (hasReplaceableItem(paragraph.getText())) {
			String replacedText = StringUtils.replace(paragraph.getText(), searchValue, replacement);

			removeAllRuns(paragraph);
			
			if (searchValue == "<DATE>" || searchValue == "<BODY>" || searchValue == "<SALUTATION>"){
				insertReplacementRuns2(paragraph, replacedText);
			}
			else if (searchValue == "<HEAD>"){
				insertReplacementRuns(paragraph, replacedText);
			}
			else if (searchValue == "<SUBJECT>"){
				insertReplacementRuns3(paragraph, replacedText);
			}
			else{
				insertReplacementRuns(paragraph, replacedText);
			}

			
		}
	}

	private void insertReplacementRuns3(XWPFParagraph paragraph, String replacedText) {
		String[] replacementTextSplitOnCarriageReturn = StringUtils.split(replacedText, "\n");

		for (int j = 0; j < replacementTextSplitOnCarriageReturn.length; j++) {
			String part = replacementTextSplitOnCarriageReturn[j];

			XWPFRun newRun = paragraph.insertNewRun(j);
			newRun.setBold(true);
			newRun.setFontSize(11);
			newRun.setText(part);

			if (j + 1 < replacementTextSplitOnCarriageReturn.length) {
				newRun.addCarriageReturn();
				newRun.addCarriageReturn();
			}
		}
	}
	
	
	private void insertReplacementRuns2(XWPFParagraph paragraph, String replacedText) {
		String[] replacementTextSplitOnCarriageReturn = StringUtils.split(replacedText, "\n");

		for (int j = 0; j < replacementTextSplitOnCarriageReturn.length; j++) {
			String part = replacementTextSplitOnCarriageReturn[j];

			XWPFRun newRun = paragraph.insertNewRun(j);
			newRun.setFontSize(11);
			newRun.setText(part);

			if (j + 1 < replacementTextSplitOnCarriageReturn.length) {
				newRun.addCarriageReturn();
				newRun.addCarriageReturn();
			}
		}
	}
	
	private void insertReplacementRuns(XWPFParagraph paragraph, String replacedText) {
		String[] replacementTextSplitOnCarriageReturn = StringUtils.split(replacedText, "\n");

		for (int j = 0; j < replacementTextSplitOnCarriageReturn.length; j++) {
			String part = replacementTextSplitOnCarriageReturn[j];

			XWPFRun newRun = paragraph.insertNewRun(j);
			newRun.setFontSize(11);
			newRun.setText(part);

			if (j + 1 < replacementTextSplitOnCarriageReturn.length) {
				newRun.addCarriageReturn();
			}
		}
	}

	private void removeAllRuns(XWPFParagraph paragraph) {
		int size = paragraph.getRuns().size();
		for (int i = 0; i < size; i++) {
			paragraph.removeRun(0);
		}
	}

	private boolean hasReplaceableItem(String runText) {
		return StringUtils.contains(runText, searchValue);
	}
}